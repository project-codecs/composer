package com.codex.composer.api.v1.tooltips.layout;

import com.codex.composer.api.v1.tooltips.TooltipContext;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.lilbrocodes.constructive.api.v1.anno.Constructive;
import org.lilbrocodes.constructive.api.v1.anno.builder.*;

import java.util.List;
import java.util.function.Function;

//? if minecraft: >=1.21.5
//import java.util.function.Consumer;

/**
 * Represents a single tooltip section with optional nested sections.
 */
@SuppressWarnings("UnusedAssignment")
@Constructive(builder = true)
public class Section {
    private final String title;
    @Default @NullCheck(check = "%f.isBlank()") private String details = "details";
    @Name(name = "keyCombo") private final Function<TooltipContext, Modifier> requiredButtonProvider;
    private final ContentProvider content;
    @Builder @Name(name = "children") private final List<Section> nestedSections;
    @Builder private final List<Formatting> titleFormat;
    @Builder @Default private List<Formatting> contentFormat = List.of(Formatting.GRAY);
    @Builder @Default private List<Formatting> hiddenFormat = List.of(Formatting.GRAY);

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
    //? if minecraft: <=1.21.4
    public void append(TooltipContext context, List<Text> out) {
    //? if minecraft: >=1.21.5
    //public void append(TooltipContext context, Consumer<Text> out) {
        Modifier requiredButtons = requiredButtonProvider.apply(context);

        boolean buttonsPressed = requiredButtons == null || requiredButtons.matches(context);

        if (!buttonsPressed) {
            out./*? if minecraft: <=1.21.4 {*/add/*? } else {*//*accept*//*? }*/(Text.translatable("composer.dynamic_tooltips.hidden", requiredButtons.toString(), Text.translatable(details)).formatted(hiddenFormat.toArray(new Formatting[]{})));
            return;
        }

        if (!title.isBlank()) out./*? if minecraft: <=1.21.4 {*/add/*? } else {*//*accept*//*? }*/(Text.translatable(title).formatted(titleFormat.toArray(new Formatting[]{})));

        if (content != null) {
            String main = content.get(context);
            if (main != null && !main.isEmpty()) {
                out./*? if minecraft: <=1.21.4 {*/add/*? } else {*//*accept*//*? }*/(Text.translatable(main).formatted(contentFormat.toArray(new Formatting[]{})));
            }
        }

        if (nestedSections != null) {
            for (Section child : nestedSections) {
                child.append(context, out);
            }
        }
    }
}