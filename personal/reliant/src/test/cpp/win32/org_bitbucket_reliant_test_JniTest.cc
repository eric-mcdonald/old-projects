/*
 * org_bitbucket_reliant_test_JniTest.cc
 *
 *  Created on: Dec 12, 2016
 *      Author: Eric McDonald
 */




#include "org_bitbucket_reliant_test_JniTest.h"

#include <iostream>

JNIEXPORT void JNICALL Java_org_bitbucket_reliant_test_JniTest_test
  (JNIEnv *, jobject) {
	std::cout << "Test." << std::endl;
}
