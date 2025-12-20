package com.example.customexecutablerunner

import java.io.File

object ExecutableResolver {

    fun resolve(option: ExecutableOption, customPath: String): String {
        return when (option) {
            ExecutableOption.RUSTC -> findInPath("rustc")
            ExecutableOption.CARGO -> findInPath("cargo")
            ExecutableOption.CUSTOM -> customPath
        }
    }

    private fun findInPath(command: String): String {
        val paths = System.getenv("PATH") ?: return command

        for (dir in paths.split(File.pathSeparator)) {
            val file = File(dir, command)
            if (file.exists() && file.canExecute()) {
                return file.absolutePath
            }
        }
        return command
    }
}