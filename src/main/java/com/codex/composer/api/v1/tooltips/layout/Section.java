package com.codex.composer.api.v1.tooltips;

import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.lilbrocodes.constructive.api.v1.anno.Constructive;
import org.lilbrocodes.constructive.api.v1.anno.builder.*;

import java.util.List;
import java.util.function.Function;

/**
 * Represents a single tooltip section with optional nested sections.
 */
@Constructive(builder = true)
public class Section {
    private final String title;
    @Default
    @NullCheck(check = "%f.isBlank()")
    private String details = "details";
    @Name(name = "keyCombo")
    private final Function<TooltipContext, Modifier> requiredButtonProvider;
    private final ContentProvider content;
    @Builder
    @Name(name = "children")
    private final List<Section> nestedSections;
    @Builder
    private final List<Formatting> titleFormat;
    @Builder
    @Default
    private List<Formatting> contentFormat = List.of(Formatting.GRAY);
    @Builder
    @Default
    private List<Formatting> hiddenFormat = List.of(Formatting.GRAY);

    Section(String title, String details, Function<TooltipContext, Modifier> requiredButtonProvider, ContentProvider content, List<Section> nestedSections, List<Formatting> titleFormat, List<Formatting> contentFormat, List<Formatting> hiddenFormat) {
        this.title = title;
        this.details = details;
        this.requiredButtonProvider = requiredButtonProvider;
        this.content = content;
        this.nestedSections = nestedSections;
        this.titleFormat = titleFormat;
        this.contentFormat = contentFormat;
        this.hiddenFormat = hiddenFormat;
    }

    /**
     * Appends this section to the output list based on context.
     */
    public void append(TooltipContext context, List<Text> out) {
        Modifier requiredButtons = requiredButtonProvider.apply(context);

        boolean buttonsPressed = requiredButtons == null || requiredButtons.matches(context);

        if (!buttonsPressed) {
            out.add(Text.translatable("composer.dynamic_tooltips.hidden", requiredButtons.toString(), Text.translatable(details)).formatted(hiddenFormat.toArray(new Formatting[]{})));
            return;
        }

        if (!title.isBlank()) out.add(Text.translatable(title).formatted(titleFormat.toArray(new Formatting[]{})));

        if (content != null) {
            String main = content.get(context);
            if (main != null && !main.isEmpty()) {
                out.add(Text.translatable(main).formatted(contentFormat.toArray(new Formatting[]{})));
            }
        }

        if (nestedSections != null) {
            for (Section child : nestedSections) {
                child.append(context, out);
            }
        }
    }
}