package net.goat.mixin;

import net.goat.badge.BadgeStyle;
import net.goat.badge.BadgeStyleRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityNameMixin {

    @Inject(method = "getDisplayName", at = @At("RETURN"), cancellable = true)
    private void pinpoint_combat$decorateName(CallbackInfoReturnable<Text> cir) {
        PlayerEntity self = (PlayerEntity) (Object) this;

        Identifier badgeItemId = BadgeState.get(self.getUuid());
        if (badgeItemId == null) return;

        BadgeStyle style = BadgeStyleRegistry.getByItemId(badgeItemId);
        if (style == null) return;

        cir.setReturnValue(style.decorate(cir.getReturnValue()));
    }
}
