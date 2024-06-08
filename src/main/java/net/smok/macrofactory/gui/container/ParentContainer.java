package net.smok.macrofactory.gui.container;

import com.google.common.annotations.VisibleForTesting;
import net.minecraft.client.gui.*;
import net.minecraft.client.gui.navigation.GuiNavigation;
import net.minecraft.client.gui.navigation.GuiNavigationPath;
import net.minecraft.client.gui.navigation.NavigationDirection;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface ParentContainer extends ParentElement, Drawable, Selectable {

    @Override
    default boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (getFocused() != null && getFocused().keyPressed(keyCode, scanCode, modifiers)) return true;

        GuiNavigation guiNavigation = getNavigation(keyCode);
        if (guiNavigation != null) {
            GuiNavigationPath guiNavigationPath = getNavigationPath(guiNavigation);
            if (guiNavigationPath == null && guiNavigation instanceof GuiNavigation.Tab) {
                this.blur();
                guiNavigationPath = getNavigationPath(guiNavigation);
            }

            if (guiNavigationPath != null) {
                this.switchFocus(guiNavigationPath);
                return true;
            }
        }

        return false;
    }

    default void blur() {
        GuiNavigationPath guiNavigationPath = this.getFocusedPath();
        if (guiNavigationPath != null) {
            guiNavigationPath.setFocused(false);
        }

    }

    @VisibleForTesting
    default void switchFocus(GuiNavigationPath path) {
        this.blur();
        path.setFocused(true);
    }

    @Nullable
    default GuiNavigation getNavigation(int keyCode) {
        return switch (keyCode) {
            case 258 -> new GuiNavigation.Tab(!Screen.hasShiftDown());
            default -> null;
            case 262 -> new GuiNavigation.Arrow(NavigationDirection.RIGHT);
            case 263 -> new GuiNavigation.Arrow(NavigationDirection.LEFT);
            case 264 -> new GuiNavigation.Arrow(NavigationDirection.DOWN);
            case 265 -> new GuiNavigation.Arrow(NavigationDirection.UP);
        };
    }


    @NotNull
    ParentElement getParent();


    @Override
    default void setFocused(boolean focused) {
        if (!focused) focusOn(null);
    }


    @Override
    default boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (isFocused()) {
            //noinspection DataFlowIssue
            if (getFocused().mouseClicked(mouseX, mouseY, button)) return true;
            else focusOn(null);
        }

        for (Element child : children()) {
            if (child.mouseClicked(mouseX, mouseY, button)) {
                focusOn(child);
                if (button == 0) setDragging(true);
                return true;
            }
        }

        return false;
    }

    @Override
    default boolean mouseReleased(double mouseX, double mouseY, int button) {
        setDragging(false);
        if (getFocused() != null) {

            boolean breaking = getFocused().mouseReleased(mouseX, mouseY, button);
            if (!breaking) focusOn(null);
            return breaking;
        }
        return ParentElement.super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    List<? extends Element> children();

    List<? extends Drawable> drawables();

    <C extends Element> Element addElement(@NotNull C elementChild);

    <C extends Drawable> Drawable addDrawable(@NotNull C drawableChild);

    default <C extends Drawable & Element> void addDrawableElement(C child) {
        addElement(child);
        addDrawable(child);
    }

    @Override
    default void focusOn(@Nullable Element element) {
        if (getFocused() != null) getFocused().setFocused(false);
        setFocused(element);
        if (element != null) element.setFocused(true);
    }

    @Override
    default void render(DrawContext context, int mouseX, int mouseY, float delta) {
        for (Drawable drawable : drawables()) drawable.render(context, mouseX, mouseY, delta);
    }

    @Override
    default SelectionType getType() {
        return SelectionType.NONE;
    }


    @Override
    default void appendNarrations(NarrationMessageBuilder builder) {
        for (Element element : children()) {
            if (element instanceof Selectable selectable) (selectable).appendNarrations(builder);
        }
    }

}
