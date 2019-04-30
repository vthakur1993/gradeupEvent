# gradeupEvent
Initialize GradeUp.init(context)
To send event: GradeUp.sendEvent("Testing", map)



allprojects {
	repositories {
		...
		maven { url 'https://jitpack.io' }
	}
}
dependencies {
  implementation 'com.github.vthakur1993:gradeupEvent:Tag'
}
