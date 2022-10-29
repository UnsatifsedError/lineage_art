/*
 * Copyright (C) 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package android.test.app;

import android.test.lib.TestUtils;
import android.test.productsharedlib.ProductSharedLib;
import android.test.systemextsharedlib.SystemExtSharedLib;
import android.test.systemsharedlib.SystemSharedLib;
import android.test.vendorsharedlib.VendorSharedLib;
import androidx.test.filters.MediumTest;
import androidx.test.runner.AndroidJUnit4;
import org.junit.Test;
import org.junit.runner.RunWith;

@MediumTest
@RunWith(AndroidJUnit4.class)
public class DataAppTest {
    @Test
    public void testLoadExtendedPublicLibraries() {
        System.loadLibrary("foo.oem1");
        System.loadLibrary("bar.oem1");
        System.loadLibrary("foo.oem2");
        TestUtils.assertLinkerNamespaceError(
                () -> System.loadLibrary("bar.oem2")); // Missing <uses-native-library>.
        System.loadLibrary("foo.product1");
        System.loadLibrary("bar.product1");
    }

    @Test
    public void testLoadPrivateLibraries() {
        TestUtils.assertLinkerNamespaceError(() -> System.loadLibrary("system_private1"));
        TestUtils.assertLinkerNamespaceError(() -> System.loadLibrary("systemext_private1"));
        TestUtils.assertLibraryNotFound(() -> System.loadLibrary("product_private1"));
        TestUtils.assertLibraryNotFound(() -> System.loadLibrary("vendor_private1"));
    }

    @Test
    public void testLoadPrivateLibrariesViaSystemSharedLib() {
        SystemSharedLib.loadLibrary("system_private2");
        SystemSharedLib.loadLibrary("systemext_private2");
        TestUtils.assertLibraryNotFound(() -> SystemSharedLib.loadLibrary("product_private2"));
        TestUtils.assertLibraryNotFound(() -> SystemSharedLib.loadLibrary("vendor_private2"));
    }

    @Test
    public void testLoadPrivateLibrariesViaSystemExtSharedLib() {
        SystemExtSharedLib.loadLibrary("system_private3");
        SystemExtSharedLib.loadLibrary("systemext_private3");
        TestUtils.assertLibraryNotFound(() -> SystemExtSharedLib.loadLibrary("product_private3"));
        TestUtils.assertLibraryNotFound(() -> SystemExtSharedLib.loadLibrary("vendor_private3"));
    }

    @Test
    public void testLoadPrivateLibrariesViaProductSharedLib() {
        TestUtils.assertLinkerNamespaceError(() -> ProductSharedLib.loadLibrary("system_private4"));
        TestUtils.assertLinkerNamespaceError(
                () -> ProductSharedLib.loadLibrary("systemext_private4"));
        ProductSharedLib.loadLibrary("product_private4");
        TestUtils.assertLibraryNotFound(() -> ProductSharedLib.loadLibrary("vendor_private4"));
    }

    @Test
    public void testLoadPrivateLibrariesViaVendorSharedLib() {
        TestUtils.assertLinkerNamespaceError(() -> VendorSharedLib.loadLibrary("system_private5"));
        TestUtils.assertLinkerNamespaceError(
                () -> VendorSharedLib.loadLibrary("systemext_private5"));
        TestUtils.assertLibraryNotFound(() -> VendorSharedLib.loadLibrary("product_private5"));
        VendorSharedLib.loadLibrary("vendor_private5");
    }
}