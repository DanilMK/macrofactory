package net.smok.macrofactory.macros;

import org.jetbrains.annotations.Nullable;

public record DeleteAction(@Nullable Macro macro, @Nullable Module module) {

    public void undo() {
        if (macro != null) macro.markAsDeleted = false;
        if (module != null) module.markAsDeleted = false;
    }
}
