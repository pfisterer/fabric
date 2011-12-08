## Makefile for the cpp exmaples


compile:

	@echo =================================
	@echo COMPILE
	@echo =================================
	
	g++ Empty.cpp -o empty;
	g++ Simple.cpp -o simple;
	g++ Two.cpp -o two;
	g++ Nested.cpp -o nested;
	g++ NestedOfNested.cpp -o nestedOfNested;
	g++ ConstructorDestructor.cpp -o conDes;
	g++ Struct.cpp -o struct;
	g++ Enum.cpp -o enum;
	g++ Directives.cpp -o directives;
	g++ Typedef.cpp -o typedef;
	g++ Inheritance.cpp -o inheritance;
	g++ NestedOfNestedWithConstructor.cpp -o nestedOfNestedWithC;
	 
run:

	@echo =================================
	@echo RUN
	@echo =================================

	./empty;
	./simple;
	./two;
	./nested;
	./nestedOfNested;
	./conDes;
	./struct;
	./enum;
	./directives;
	./typedef;
	./inheritance;
	./nestedOfNestedWithC;

clean:

	@echo =================================
	@echo CLEAN
	@echo =================================

	rm -f *.cpp;
	rm -f *.hpp;
	rm ./empty ./simple ./two ./nested ./nestedOfNested ./conDes ./struct ./enum ./directives ./typedef ./inheritance ./nestedOfNestedWithC

