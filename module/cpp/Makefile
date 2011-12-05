## Makefile for the cpp exmaples


compile:

	@echo =================================
	@echo COMPILE
	@echo =================================
	
	g++ CRectangleSimple.cpp -o simple
	g++ Two.cpp -o two
	g++ Nested.cpp -o nested
	g++ NestedOfNested.cpp -o nestedOfNested
	g++ Person.cpp -o person
	g++ StructSample.cpp -o struct
	g++ EnumSample.cpp -o enum
	g++ Directives.cpp -o directives
	g++ TypedefSample.cpp -o typedef
	 
run:

	@echo =================================
	@echo RUN
	@echo =================================

	./simple;
	./two;
	./nested;
	./nestedOfNested;
	./person;
	./struct;
	./enum;
	./directives
	./typedef

clean:

	@echo =================================
	@echo CLEAN
	@echo =================================


	rm -f *.cpp;
	rm -f *.hpp;
	rm ./simple ./two ./nested ./nestedOfNested ./person ./struct ./enum ./directives ./typedef

