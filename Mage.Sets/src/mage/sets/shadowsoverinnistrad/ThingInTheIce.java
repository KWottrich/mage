/*
 *  Copyright 2010 BetaSteward_at_googlemail.com. All rights reserved.
 *
 *  Redistribution and use in source and binary forms, with or without modification, are
 *  permitted provided that the following conditions are met:
 *
 *     1. Redistributions of source code must retain the above copyright notice, this list of
 *        conditions and the following disclaimer.
 *
 *     2. Redistributions in binary form must reproduce the above copyright notice, this list
 *        of conditions and the following disclaimer in the documentation and/or other materials
 *        provided with the distribution.
 *
 *  THIS SOFTWARE IS PROVIDED BY BetaSteward_at_googlemail.com ``AS IS'' AND ANY EXPRESS OR IMPLIED
 *  WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 *  FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL BetaSteward_at_googlemail.com OR
 *  CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 *  CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 *  SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 *  ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 *  NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
 *  ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 *  The views and conclusions contained in the software and documentation are those of the
 *  authors and should not be interpreted as representing official policies, either expressed
 *  or implied, of BetaSteward_at_googlemail.com.
 */
package mage.sets.shadowsoverinnistrad;

import java.util.UUID;
import mage.MageInt;
import mage.abilities.Ability;
import mage.abilities.common.EntersBattlefieldAbility;
import mage.abilities.common.SpellCastControllerTriggeredAbility;
import mage.abilities.condition.common.SourceHasCounterCondition;
import mage.abilities.decorator.ConditionalOneShotEffect;
import mage.abilities.effects.Effect;
import mage.abilities.effects.common.TransformSourceEffect;
import mage.abilities.effects.common.counter.AddCountersSourceEffect;
import mage.abilities.effects.common.counter.RemoveCounterSourceEffect;
import mage.abilities.keyword.DefenderAbility;
import mage.abilities.keyword.TransformAbility;
import mage.cards.CardImpl;
import mage.constants.CardType;
import mage.constants.Rarity;
import mage.counters.CounterType;
import mage.filter.FilterSpell;
import mage.filter.predicate.Predicates;
import mage.filter.predicate.mageobject.CardTypePredicate;

/**
 *
 * @author fireshoes
 */
public class ThingInTheIce extends CardImpl {

    private static final FilterSpell filter = new FilterSpell("an instant or sorcery spell");

    static {
        filter.add(Predicates.or(
                new CardTypePredicate(CardType.INSTANT),
                new CardTypePredicate(CardType.SORCERY)));
    }

    public ThingInTheIce(UUID ownerId) {
        super(ownerId, 92, "Thing in the Ice", Rarity.RARE, new CardType[]{CardType.CREATURE}, "{1}{U}");
        this.expansionSetCode = "SOI";
        this.subtype.add("Horror");
        this.power = new MageInt(0);
        this.toughness = new MageInt(4);

        this.canTransform = true;
        this.secondSideCard = new AwokenHorror(ownerId);

        // Defender
        this.addAbility(DefenderAbility.getInstance());

        // Thing in the Ice enters the battlefield with four ice counters on it.
        Effect effect = new AddCountersSourceEffect(CounterType.ICE.createInstance(4));
        effect.setText("with four ice counters on it");
        this.addAbility(new EntersBattlefieldAbility(effect));

        // Whenever you cast an instant or sorcery spell, remove an ice counter from Thing in the Ice. Then if it has no ice counters on it, transform it.
        this.addAbility(new TransformAbility());
        effect = new RemoveCounterSourceEffect(CounterType.ICE.createInstance(1));
        effect.setText("remove an ice counter from {this}");
        Ability ability = new SpellCastControllerTriggeredAbility(effect, filter, false);
        effect = new ConditionalOneShotEffect(new TransformSourceEffect(true), new SourceHasCounterCondition(CounterType.ICE, 0, 0),
                "if there are no ice counters on it, transform it");
        ability.addEffect(effect);
        this.addAbility(ability);

    }

    public ThingInTheIce(final ThingInTheIce card) {
        super(card);
    }

    @Override
    public ThingInTheIce copy() {
        return new ThingInTheIce(this);
    }
}
