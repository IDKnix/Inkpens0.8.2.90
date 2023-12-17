@echo off

echo COMPILING.

kotlinc Main_082.kt Inkpacks/inkcloud_080.kt -include-runtime -d ink.jar

echo Done. Execute with java -jar ink.jar.