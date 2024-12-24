plugins {
    id("dev.deftu.gradle.multiversion-root")
}

preprocess {
    "1.12.2-fabric"(11202, "yarn") {
        "1.12.2-forge"(11202, "srg") {
            "1.8.9-forge"(10809, "srg", file("versions/1.12.2-forge+1.8.9-forge.txt")) {
                "1.8.9-fabric"(10809, "yarn")
            }
        }
    }
}