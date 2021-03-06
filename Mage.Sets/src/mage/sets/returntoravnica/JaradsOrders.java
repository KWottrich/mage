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
package mage.sets.returntoravnica;

import java.util.List;
import java.util.UUID;
import mage.abilities.Ability;
import mage.abilities.effects.OneShotEffect;
import mage.cards.Card;
import mage.cards.CardImpl;
import mage.cards.Cards;
import mage.cards.CardsImpl;
import mage.constants.CardType;
import mage.constants.Outcome;
import mage.constants.Rarity;
import mage.constants.Zone;
import mage.filter.FilterCard;
import mage.filter.common.FilterCreatureCard;
import mage.game.Game;
import mage.players.Player;
import mage.target.TargetCard;
import mage.target.common.TargetCardInLibrary;

/**
 *
 * @author LevelX2
 */
public class JaradsOrders extends CardImpl {

    public JaradsOrders(UUID ownerId) {
        super(ownerId, 175, "Jarad's Orders", Rarity.RARE, new CardType[]{CardType.SORCERY}, "{2}{B}{G}");
        this.expansionSetCode = "RTR";

        // Search your library for up to two creature cards and reveal them. Put one into your hand and the other into your graveyard. Then shuffle your library.
        this.getSpellAbility().addEffect(new JaradsOrdersEffect());
    }

    public JaradsOrders(final JaradsOrders card) {
        super(card);
    }

    @Override
    public JaradsOrders copy() {
        return new JaradsOrders(this);
    }
}
class JaradsOrdersEffect extends OneShotEffect {

    protected static final FilterCard filter = new FilterCard("card to put into your hand");

    public JaradsOrdersEffect() {
        super(Outcome.PutLandInPlay);
        staticText = "Search your library for up to two creature cards and reveal them. Put one into your hand and the other into your graveyard. Then shuffle your library";
    }

    public JaradsOrdersEffect(final JaradsOrdersEffect effect) {
        super(effect);
    }

    @Override
    public JaradsOrdersEffect copy() {
        return new JaradsOrdersEffect(this);
    }

    @Override
    public boolean apply(Game game, Ability source) {        
        Player controller = game.getPlayer(source.getControllerId());
        if (controller != null) {
            TargetCardInLibrary target = new TargetCardInLibrary(0, 2, new FilterCreatureCard("creature cards"));
            if (controller.searchLibrary(target, game)) {
                if (target.getTargets().size() > 0) {
                    Cards revealed = new CardsImpl();
                    for (UUID cardId: (List<UUID>)target.getTargets()) {
                        Card card = controller.getLibrary().getCard(cardId, game);
                        revealed.add(card);
                    }
                    controller.revealCards("Jarad's Orders", revealed, game);
                    if (target.getTargets().size() == 2) {
                        TargetCard target2 = new TargetCard(Zone.LIBRARY, filter);
                        controller.choose(Outcome.Benefit, revealed, target2, game);
                        Card card = revealed.get(target2.getFirstTarget(), game);
                        controller.moveCards(card, Zone.LIBRARY, Zone.HAND, source, game);
                        revealed.remove(card);
                        card = revealed.getCards(game).iterator().next();
                        controller.moveCards(card, Zone.LIBRARY, Zone.GRAVEYARD, source, game);
                    } else if (target.getTargets().size() == 1) {
                        Card card = revealed.getCards(game).iterator().next();
                        controller.moveCards(card, Zone.LIBRARY, Zone.HAND, source, game);
                    }

                }
                controller.shuffleLibrary(source, game);
                return true;
            }
            controller.shuffleLibrary(source, game);
        }
        return false;

    }

}
