/*     / \____  _    _  ____   ______  / \ ____  __    _______
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  //  /\__\   JΛVΛSLΛNG
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/ \ /__\ \   Copyright 2014-2016 Javaslang, http://javaslang.io
 * /___/\_/  \_/\____/\_/  \_/\__\/__/\__\_/  \_//  \__/\_____/   Licensed under the Apache License, Version 2.0
 */
package client;

import com.google.gwt.junit.tools.GWTTestSuite;
import junit.framework.Test;
import junit.framework.TestCase;

public class GwtJavaslangTestSuite extends TestCase {

    public static Test suite() {
        GWTTestSuite suite = new GWTTestSuite("Javaslang test suite.");
        suite.addTestSuite(CollectionsTestGwt.class);
        suite.addTestSuite(ConcurrentTestGwt.class);
        return suite;
    }
}
