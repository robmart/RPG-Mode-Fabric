package robmart.mod.rpgmodecore.api.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

public class LivingEntityEvents {

    public static final Event<Jump> JUMPING_EVENT = EventFactory.createArrayBacked(Jump.class,
            (listeners) -> (player) -> {
                for (Jump listener : listeners) {
                    ActionResult result = listener.onJump(player);

                    if(result != ActionResult.PASS) {
                        return result;
                    }
                }

                return ActionResult.PASS;
            });

    @FunctionalInterface
    public interface Jump {
        /**
         * Checks whether an entity can jump.
         *
         * @param entity      the jumping entity
         * @return Upon return:
         * - SUCCESS cancels further processing and continues with normal jumping behavior.
         * - PASS falls back to further processing and defaults to SUCCESS if no other listeners are available
         * - FAIL cancels further processing and does not jump.
         * @see LivingEntity#jump()
         */
        @Nullable
        ActionResult onJump(LivingEntity entity);
    }
}
