//package robmart.mod.rpgmodecreatures.common.entity.ai.goal;
//
//import net.minecraft.entity.mob.MobEntity;
//import net.minecraft.util.math.Box;
//import org.jetbrains.annotations.Nullable;
//
//import java.util.function.Predicate;
//
//public class FollowTargetHeightGoal extends FollowTargetGoal {
//    public FollowTargetHeightGoal(MobEntity mob, Class targetClass, boolean checkVisibility) {
//        this(mob, targetClass, checkVisibility, false);
//    }
//
//    public FollowTargetHeightGoal(MobEntity mob, Class targetClass, boolean checkVisibility, boolean checkCanNavigate) {
//        this(mob, targetClass, 10, checkVisibility, checkCanNavigate, null);
//    }
//
//    public FollowTargetHeightGoal(MobEntity mob, Class targetClass, int reciprocalChance, boolean checkVisibility, boolean checkCanNavigate, @Nullable Predicate targetPredicate) {
//        super(mob, targetClass, reciprocalChance, checkVisibility, checkCanNavigate, targetPredicate);
//    }
//
//    @Override
//    protected Box getSearchBox(double distance) {
//        return this.mob.getBoundingBox().expand(distance, distance, distance);
//    }
//}
