package de.uniluebeck.sourcegen.c;

public class CppHelper {


    public static void toStringHelper(StringBuffer buffer, CppClass clazz, int tabCount, boolean isLast) {

        // Public constructors
        if (null != clazz.getConstructors(Cpp.PUBLIC) && clazz.getConstructors(Cpp.PUBLIC).size() > 0) {
            for (CppConstructor c : clazz.getConstructors(Cpp.PUBLIC)) {
                buffer.append(Cpp.newline + Cpp.newline);
                c.toString(buffer, tabCount);
            }
        }

        // Private constructors
        if (null != clazz.getConstructors(Cpp.PRIVATE) && clazz.getConstructors(Cpp.PRIVATE).size() > 0) {
            for (CppConstructor c : clazz.getConstructors(Cpp.PRIVATE)) {
                buffer.append(Cpp.newline + Cpp.newline);
                c.toString(buffer, tabCount);
            }
        }

        // Public destructors
        if (null != clazz.getDestructors(Cpp.PUBLIC) && clazz.getDestructors(Cpp.PUBLIC).size() > 0) {
            for (CppDestructor d : clazz.getDestructors(Cpp.PUBLIC)) {
                d.toString(buffer, tabCount);
            }
        }

        // Private destructors
        if (null != clazz.getDestructors(Cpp.PRIVATE) && clazz.getDestructors(Cpp.PRIVATE).size() > 0) {
            for (CppDestructor d : clazz.getDestructors(Cpp.PRIVATE)) {
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
            if (!isLast) {
                buffer.append(Cpp.newline + Cpp.newline);
            }
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
            if (!isLast) {
                buffer.append(Cpp.newline + Cpp.newline);
            }
        }

        // Public nested classes
        if (null != clazz.getNested(Cpp.PUBLIC) && clazz.getNested(Cpp.PUBLIC).size() > 0) {
            for (int i = 0; i < clazz.getNested(Cpp.PUBLIC).size(); ++i) {
                boolean last = (i == clazz.getNested(Cpp.PUBLIC).size() - 1) &&
                        (clazz.getNested(Cpp.PROTECTED).size() <= 0) &&
                        (clazz.getNested(Cpp.PRIVATE).size() <= 0);
                toStringHelper(buffer, clazz.getNested(Cpp.PUBLIC).get(i), tabCount, last);
            }
        }

        // Protected nested classes
        if (null != clazz.getNested(Cpp.PROTECTED) && clazz.getNested(Cpp.PROTECTED).size() > 0) {
            for (int i = 0; i < clazz.getNested(Cpp.PROTECTED).size(); ++i) {
                boolean last = (i == clazz.getNested(Cpp.PROTECTED).size() - 1) &&
                        (clazz.getNested(Cpp.PRIVATE).size() <= 0);
                toStringHelper(buffer, clazz.getNested(Cpp.PROTECTED).get(i), tabCount, last);
            }
        }

        // Private nested classes
        if (null != clazz.getNested(Cpp.PRIVATE) && clazz.getNested(Cpp.PRIVATE).size() > 0) {
            for (int i = 0; i < clazz.getNested(Cpp.PRIVATE).size(); ++i) {
                boolean last = (i == clazz.getNested(Cpp.PRIVATE).size() - 1);
                toStringHelper(buffer, clazz.getNested(Cpp.PRIVATE).get(i), tabCount, last);
            }
        }

    }

}
