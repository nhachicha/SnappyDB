Change Log
==========

Version 0.5.1 *(2015-04-15)*
----------------------------
 * Providing 64-bit ABIs

 * Targeting android-9 as minSdk (NDK)

 * Update build tools: Gradle "2.2", Android plugin "1.1.3", compileSdkVersion "22", buildToolsVersion "22.0.1"


Version 0.5.0 *(2014-10-14)*
----------------------------
 * Add offset & limit for keys search operations

 * Efficient keys count
 
 * Iterators for pagination & traversing a large keys collection
 
 * Update to gradle 2.1 
 
 
Version 0.4.0 *(2014-09-13)*
----------------------------

 * Moving to Gradle build system. (fix issue #2)

 * Moving Kryo jars as Gradle dependency instead of embedding inside libs (fix issue #13)

 * API enhancement: search by prefix & range (fix issue #7)

 * Allow registration of custom serializers for Kryo (fix issue #6) contributed by @DayS

 * Remove the need to implement Serializable to store Objects & Arrays 

