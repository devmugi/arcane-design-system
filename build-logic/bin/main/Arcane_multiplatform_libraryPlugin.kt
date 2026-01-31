/**
 * Precompiled [arcane.multiplatform.library.gradle.kts][Arcane_multiplatform_library_gradle] script plugin.
 *
 * @see Arcane_multiplatform_library_gradle
 */
public
class Arcane_multiplatform_libraryPlugin : org.gradle.api.Plugin<org.gradle.api.Project> {
    override fun apply(target: org.gradle.api.Project) {
        try {
            Class
                .forName("Arcane_multiplatform_library_gradle")
                .getDeclaredConstructor(org.gradle.api.Project::class.java, org.gradle.api.Project::class.java)
                .newInstance(target, target)
        } catch (e: java.lang.reflect.InvocationTargetException) {
            throw e.targetException
        }
    }
}
