package com.example.customexecutablerunner

enum class ExecutableOption(val presentableName: String) {
    RUSTC("Rust Compiler (rustc)"),
    CARGO("Cargo"),
    CUSTOM("Custom executable");

    override fun toString(): String = presentableName
}