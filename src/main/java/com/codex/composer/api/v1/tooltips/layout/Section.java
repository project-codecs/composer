package com.codex.composer.api.v1.tooltips.layout;

import com.codex.ambarella.api.v1.util.collections.ListBuilder;
import com.codex.composer.api.v1.tooltips.TooltipContext;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.List;
import java.util.function.Function;

//? if minecraft: >=1.21.5
//import java.util.function.Consumer;

/**
 * Represents a single tooltip section with optional nested sections.
 */
public class Section {
    private final String title;
    private final Text details;
    private final Function<TooltipContext, Modifier> requiredButtonProvider;
    private final Function<TooltipContext, List<Text>> content;
    private final List<Section> nestedSections;
    private final List<Formatting> titleFormat;
    private final List<Formatting> contentFormat;
    private final List<Formatting> hiddenFormat;

    Section(String title, Text details, Function<TooltipContext, Modifier> requiredButtonProvider, Function<TooltipContext, List<Text>> content, List<Section> nestedSections, List<Formatting> titleFormat, List<Formatting> contentFormat, List<Formatting> hiddenFormat) {
        this.title = title;
        this.details = details;
        this.requiredButtonProvider = requiredButtonProvider;
        this.content = content;
        this.nestedSections = nestedSections;
        this.titleFormat = titleFormat;
        this.contentFormat = contentFormat;
        this.hiddenFormat = hiddenFormat;
    }

    public static Builder builder() {
        return new Builder();
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
            out./*? if minecraft: <=1.21.4 {*/add/*? } else {*//*accept*//*? }*/(Text.translatable("composer.dynamic_tooltips.hidden", requiredButtons.toString(), details).formatted(hiddenFormat.toArray(new Formatting[]{})));
            return;
        }

        if (!title.isBlank()) out./*? if minecraft: <=1.21.4 {*/add/*? } else {*//*accept*//*? }*/(Text.translatable(title).formatted(titleFormat.toArray(new Formatting[]{})));

        if (content != null) {
            List<Text> ct = content.apply(context);
            if (ct != null && !ct.isEmpty()) {
                for (Text line : ct) out./*? if minecraft: <=1.21.4 {*/add/*? } else {*//*accept*//*? }*/(line.copy().formatted(contentFormat.toArray(new Formatting[]{})));
            }
        }

        if (nestedSections != null) {
            for (Section child : nestedSections) {
                child.append(context, out);
            }
        }
    }

    public static class Builder {
        private String title = "";

        private Text details = Text.translatable("composer.dynamic_tooltips.details");
        private Function<TooltipContext, Modifier> keyCombo;
        private Function<TooltipContext, List<Text>> content;
        private ListBuilder<Builder, Section> children = ListBuilder.of(this);
        private ListBuilder<Builder, Formatting> titleFormat = ListBuilder.of(this);
        private ListBuilder<Builder, Formatting> contentFormat = ListBuilder.of(this, List.of(Formatting.GRAY));
        private ListBuilder<Builder, Formatting> hiddenFormat = ListBuilder.of(this, List.of(Formatting.GRAY));

        private Builder() {
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder details(Text details) {
            this.details = details;
            return this;
        }

        public Builder modifier(Function<TooltipContext, Modifier> keyCombo) {
            this.keyCombo = keyCombo;
            return this;
        }

        public Builder content(Function<TooltipContext, List<Text>> content) {
            this.content = content;
            return this;
        }

        public ListBuilder<Builder, Section> children() {
            return this.children;
        }

        public ListBuilder<Builder, Formatting> titleFormat() {
            return this.titleFormat;
        }

        public ListBuilder<Builder, Formatting> contentFormat() {
            return this.contentFormat;
        }

        public ListBuilder<Builder, Formatting> hiddenFormat() {
            return this.hiddenFormat;
        }

        public Section build() {
            if (this.title == null) {
                throw new IllegalStateException("Required field 'title' was not set");
            }

            if (this.keyCombo == null) {
                throw new IllegalStateException("Required field 'keyCombo' was not set");
            }

            if (this.content == null) {
                throw new IllegalStateException("Required field 'content' was not set");
            }

            if (this.details == null || details.getString().isBlank()) {
                this.details = Text.translatable("composer.dynamic_tooltips.details");
            }

            return new Section(title, details, keyCombo, content, children.build(), titleFormat.build(), contentFormat.build(), hiddenFormat.build());
        }

        public Builder reset() {
            this.title = null;
            this.details = Text.translatable("composer.dynamic_tooltips.details");
            this.keyCombo = null;
            this.content = null;
            this.children = children.clear();
            this.titleFormat = titleFormat.clear();
            this.contentFormat = contentFormat.clear();
            this.hiddenFormat = hiddenFormat.clear();

            return this;
        }
    }

}