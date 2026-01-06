package net.goat.mixin.client;

import net.goat.client.badge.BadgeStatusTracker;
import net.goat.client.badge.BadgeStyle;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public class PlayerEntityDisplayNameMixin {
    @Inject(method = "getDisplayName", at = @At("RETURN"), cancellable = true)
    private void pinpoint$appendBellBadge(CallbackInfoReturnable<Text> cir) {
        PlayerEntity player = (PlayerEntity) (Object) this;
        BadgeStyle style = BadgeStatusTracker.getStyle(player.getUuid());
        if (style == null) {
            return;
        }

        MutableText original = cir.getReturnValue().copy();
        MutableText coloredName = original.styled(existingStyle -> existingStyle.withColor(style.color()));
        MutableText badge = Text.literal(style.glyph()).setStyle(Style.EMPTY.withFont(style.fontId()));

        MutableText updated = coloredName.append(Text.literal(" ").setStyle(Style.EMPTY)).append(badge);
        cir.setReturnValue(updated);
    }
}
