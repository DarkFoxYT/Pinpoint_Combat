package net.goat.badge;

import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;
import net.minecraft.util.Identifier;

public record BadgeStyle(int rgb, String iconGlyph, Identifier font) {

    public MutableText decorate(Text baseName) {
        MutableText icon = Text.literal(iconGlyph)
                .styled(s -> s.withFont(font).withColor(TextColor.fromRgb(rgb)));

        MutableText coloredName = baseName.copy()
                .styled(s -> s.withColor(TextColor.fromRgb(rgb)));

        return icon.append(Text.literal(" ")).append(coloredName);
    }
}
