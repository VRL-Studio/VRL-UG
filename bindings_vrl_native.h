/* DO NOT EDIT THIS FILE - it is machine generated */
#include <jni.h>
/* Header for class edu_gcsc_vrl_ug4_UG4 */

#ifndef _Included_edu_gcsc_vrl_ug4_UG4
#define _Included_edu_gcsc_vrl_ug4_UG4
#ifdef __cplusplus
extern "C" {
#endif
/*
 * Class:     edu_gcsc_vrl_ug4_UG4
 * Method:    createJavaBindings
 * Signature: ()[Ljava/lang/String;
 */
JNIEXPORT jobjectArray JNICALL Java_edu_gcsc_vrl_ug4_UG4_createJavaBindings
  (JNIEnv *, jobject);

/*
 * Class:     edu_gcsc_vrl_ug4_UG4
 * Method:    invokeMethod
 * Signature: (Ljava/lang/String;JLjava/lang/String;[Ljava/lang/Object;)Ljava/lang/Object;
 */
JNIEXPORT jobject JNICALL Java_edu_gcsc_vrl_ug4_UG4_invokeMethod
  (JNIEnv *, jobject, jstring, jlong, jstring, jobjectArray);

/*
 * Class:     edu_gcsc_vrl_ug4_UG4
 * Method:    newInstance
 * Signature: (J)J
 */
JNIEXPORT jlong JNICALL Java_edu_gcsc_vrl_ug4_UG4_newInstance
  (JNIEnv *, jobject, jlong);

/*
 * Class:     edu_gcsc_vrl_ug4_UG4
 * Method:    getExportedClassPtrByName
 * Signature: (Ljava/lang/String;)J
 */
JNIEXPORT jlong JNICALL Java_edu_gcsc_vrl_ug4_UG4_getExportedClassPtrByName
  (JNIEnv *, jobject, jstring);

/*
 * Class:     edu_gcsc_vrl_ug4_UG4
 * Method:    invokeFunction
 * Signature: (J[Ljava/lang/Object;)Ljava/lang/Object;
 */
JNIEXPORT jobject JNICALL Java_edu_gcsc_vrl_ug4_UG4_invokeFunction
  (JNIEnv *, jobject, jlong, jobjectArray);

/*
 * Class:     edu_gcsc_vrl_ug4_UG4
 * Method:    ugInit
 * Signature: ([Ljava/lang/String;)I
 */
JNIEXPORT jint JNICALL Java_edu_gcsc_vrl_ug4_UG4_ugInit
  (JNIEnv *, jobject, jobjectArray);

/*
 * Class:     edu_gcsc_vrl_ug4_UG4
 * Method:    attachCanvas
 * Signature: (Leu/mihosoft/vrl/reflection/VisualCanvas;)V
 */
JNIEXPORT void JNICALL Java_edu_gcsc_vrl_ug4_UG4_attachCanvas
  (JNIEnv *, jobject, jobject);

#ifdef __cplusplus
}
#endif
#endif