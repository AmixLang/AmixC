/*
 *  This file is part of Robok © 2024.
 *
 *  Robok is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Robok is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *   along with Robok.  If not, see <https://www.gnu.org/licenses/>.
 */

import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
  alias(libs.plugins.kotlin)
  `java-library`
}

group = "org.amix"
version = libs.versions.lib.version.get()

java {
  sourceCompatibility = JavaVersion.VERSION_17
  targetCompatibility = JavaVersion.VERSION_17
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
  compilerOptions {
    jvmTarget.set(JvmTarget.JVM_17)
  }
}

tasks.withType<Jar> {
  duplicatesStrategy = DuplicatesStrategy.EXCLUDE
  manifest { attributes["Main-Class"] = "org.amix.fastcompiler.MainKt" }

  from({
    configurations.runtimeClasspath
      .get()
      .filter { it.exists() }
      .map { if (it.isDirectory) it else zipTree(it) }
  })
}

tasks.build { dependsOn(tasks.jar) }

dependencies {
  implementation(libs.amix)
}