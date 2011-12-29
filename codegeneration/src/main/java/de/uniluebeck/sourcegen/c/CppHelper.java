package de.uniluebeck.sourcegen.c;

public class CppHelper {


    public static void toStringHelper(StringBuffer buffer, CppClass clazz, int tabCount) {

        // Public constructors
        if (null != clazz.getConstructors(Cpp.PUBLIC) && clazz.getConstructors(Cpp.PUBLIC).size() > 0) {
            for (CppConstructor c : clazz.getConstructors(Cpp.PUBLIC)) {
                c.toString(buffer, tabCount);
            }
        }

        // Private constructors
        if (null != clazz.getConstructors(Cpp.PRIVATE) && clazz.getConstructors(Cpp.PRIVATE).size() > 0) {
            for (CppConstructor c : clazz.getConstructors(Cpp.PRIVATE)) {
                c.toString(buffer, tabCount);
            }
        }

        // Private destructors
        if (null != clazz.getDestructors(Cpp.PRIVATE) && clazz.getDestructors(Cpp.PRIVATE).size() > 0) {
            for (CppDestructor d : clazz.getDestructors(Cpp.PRIVATE)) {
                d.toString(buffer, tabCount);
            }
        }

        // Public destructors
        if (null != clazz.getDestructors(Cpp.PUBLIC) && clazz.getDestructors(Cpp.PUBLIC).size() > 0) {
            for (CppDestructor d : clazz.getDestructors(Cpp.PUBLIC)) {
                d.toString(buffer, tabCount);
            }
        }

        // Public functions
        if (null != clazz.getFuns(Cpp.PUBLIC) && clazz.getFuns(Cpp.PUBLIC).size() > 0) {
            // Does not add new lines after last function
            for (int i = 0; i < clazz.getFuns(Cpp.PUBLIC).size(); ++i) {
                CppFun f = clazz.getFuns(Cpp.PUBLIC).get(i);

                if (i < clazz.getFuns(Cpp.PUBLIC).size() - 1) {
                    f.toString(buffer, tabCount, false);
                } else {
                    f.toString(buffer, tabCount, true);
                }
            }
            buffer.append(Cpp.newline + Cpp.newline);
        }

        // Private functions
        if (null != clazz.getFuns(Cpp.PRIVATE) && clazz.getFuns(Cpp.PRIVATE).size() > 0) {
            // Does not add new lines after last function
            for (int i = 0; i < clazz.getFuns(Cpp.PRIVATE).size(); ++i) {
                CppFun f = clazz.getFuns(Cpp.PRIVATE).get(i);

                if (i < clazz.getFuns(Cpp.PRIVATE).size() - 1) {
                    f.toString(buffer, tabCount, false);
                } else {
                    f.toString(buffer, tabCount, true);
                }
            }
            buffer.append(Cpp.newline + Cpp.newline);
        }

        // Public nested classes
        if (null != clazz.getNested(Cpp.PUBLIC) && clazz.getNested(Cpp.PUBLIC).size() > 0) {
            for (CppClass c : clazz.getNested(Cpp.PUBLIC)) {
                toStringHelper(buffer, c, tabCount);
            }
        }

        // Protected nested classes
        if (null != clazz.getNested(Cpp.PROTECTED) && clazz.getNested(Cpp.PROTECTED).size() > 0) {
            for (CppClass c : clazz.getNested(Cpp.PROTECTED)) {
                toStringHelper(buffer, c, tabCount);
            }
        }

        // Private nested classes
        if (null != clazz.getNested(Cpp.PRIVATE) && clazz.getNested(Cpp.PRIVATE).size() > 0) {
            for (CppClass c : clazz.getNested(Cpp.PRIVATE)) {
                toStringHelper(buffer, c, tabCount);
            }
        }

    }

}
