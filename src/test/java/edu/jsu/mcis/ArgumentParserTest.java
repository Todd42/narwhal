package edu.jsu.mcis;

import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;

public class ArgumentParserTest {
    private ArgumentParser ap;
	
    @Before
    public void startArgumentParser(){
        ap = new ArgumentParser();
    }
	
    @Test
    public void testAddArgumentsNoDescription() {
        String one = "Test Name 1";
        String two = "Test Name 2";
        String three = "Test Name 3";
        ap.addArguments(one, ArgumentParser.Datatype.STRING);
        ap.addArguments(two, ArgumentParser.Datatype.INT);
        ap.addArguments(three, ArgumentParser.Datatype.BOOLEAN);
        String[] inp = {"Hello","5","true"};
        ap.parse(inp);
        assertEquals("Hello", (String) ap.getValue(one));
        assertEquals(5, (int) ap.getValue(two));
        assertEquals(true, (boolean) ap.getValue(three));
    }
    
    @Test
    public void testBooleanNamedArgument() {
        ap.addNamedArgument("nf1", "false", ArgumentParser.Datatype.BOOLEAN, "n", false);
        String[] inp = {"--nf1"};
        ap.parse(inp);
        assertEquals(true, ap.getValue("nf1"));
    }
    
    @Test
    public void testBooleanNamedArgumentRequired() {
        ap.addNamedArgument("nf1", "false", ArgumentParser.Datatype.BOOLEAN, "n", true);
        String[] inp = {"--nf1"};
        ap.parse(inp);
        assertEquals(true, ap.getValue("nf1"));
    }
    
    @Test
    public void testBooleanNamedArgumentUsingNickname() {
        ap.addNamedArgument("nf1", "false", ArgumentParser.Datatype.BOOLEAN, "n", false);
        String[] inp = {"-n"};
        ap.parse(inp);
        assertEquals(true, ap.getValue("nf1"));
    }
    
    @Test
    public void testAddArgumentsWithDescription() {
        String one = "Test Name 1";
        String two = "Test Name 2";
        String three = "Test Name 3";
        ap.addArguments(one, ArgumentParser.Datatype.STRING, "It's a string thing");
        ap.addArguments(two, ArgumentParser.Datatype.INT, "It's an int!");
        ap.addArguments(three, ArgumentParser.Datatype.BOOLEAN, "It is a bool");
        String[] inp = {"Hello","5","True"};
        ap.parse(inp);
        assertEquals("Hello", (String) ap.getValue(one));
        assertEquals(5, (int) ap.getValue(two));
        assertEquals(true, (boolean) ap.getValue(three));
    }
    
    @Test
    public void testParseValuesNotDashH() {
        String one = "Length";
        String two = "Width";
        String three = "Height";
        String four = "Name";
        ap.addArguments(one, ArgumentParser.Datatype.FLOAT, "Length of the object");
        ap.addArguments(two, ArgumentParser.Datatype.BOOLEAN, "Width of the object");
        ap.addArguments(three, ArgumentParser.Datatype.INT, "Height of the object");
        ap.addArguments(four, ArgumentParser.Datatype.STRING, "Name of object");
        String[] inp = {"12.34", "False", "7", "Fred"};
        ap.parse(inp);
        assertEquals(12.34, (float) ap.getValue(one), 0.01);
        assertEquals(false, (boolean) ap.getValue(two));
        assertEquals(7, (int) ap.getValue(three));
        assertEquals("Fred", (String) ap.getValue(four));   
    }
    
    @Test
    public void testAddingProgramDescriptionAndName() {
        String one = "Length";
        String two = "Width";
        String three = "Height";
        String four = "Name";
        ap.addArguments(one, ArgumentParser.Datatype.FLOAT, "Length of the object");
        ap.addArguments(two, ArgumentParser.Datatype.BOOLEAN, "Width of the object");
        ap.addArguments(three, ArgumentParser.Datatype.INT, "Height of the object");
        ap.addArguments(four, ArgumentParser.Datatype.STRING, "Name of object");
        ap.setProgramDescription("This program prints arguments");
        ap.setProgramName("ArgumentPrinter");
        String[] inp = {"12.34", "False", "7", "Fred"};
        ap.parse(inp);
        assertEquals(12.34, (float) ap.getValue(one), 0.01);
        assertEquals(false, (boolean) ap.getValue(two));
        assertEquals(7, (int) ap.getValue(three));
        assertEquals("Fred", (String) ap.getValue(four)); 
        
    }
   
    @Test
    public void testNamedArgumentDefaultValue() {
        ap.addArguments("thing", ArgumentParser.Datatype.FLOAT, "Length of the object");
        ap.addNamedArgument("stuff", "5", false);
        assertEquals("5", (String) ap.getValue("stuff"));
    }
    
    @Test
    public void testNamedArgumentDefaultValueBeingChanged() {
        ap.addArguments("thing", ArgumentParser.Datatype.FLOAT, "Length of the object");
        ap.addNamedArgument("stuff", "5", false);
        String[] inp = {"--stuff", "10", "4.6"};
        ap.parse(inp);
        assertEquals("10", (String) ap.getValue("stuff"));
        
    }  
    
    @Test
    public void testSingleArgumentNoDefaultValue() {
        ap.addArguments("thing", ArgumentParser.Datatype.FLOAT, "Length of the object");
        ap.addNamedArgument("stuff", true);
        String[] inp = {"--stuff", "5", "4.2"};
        ap.parse(inp);
        assertEquals("5", (String) ap.getValue("stuff"));
        
    }
    
    @Test
    public void testBooleanMultipleTimesTrueAndFalse() {
        ap.addArguments("Arg 1", ArgumentParser.Datatype.BOOLEAN, "This should be true");
        ap.addArguments("Arg 2", ArgumentParser.Datatype.BOOLEAN, "This should be false");
        ap.addArguments("Arg 3", ArgumentParser.Datatype.BOOLEAN, "This should be true");
        String[] inp = {"true", "false", "true"};
        ap.parse(inp);
        assertEquals(true, (boolean) ap.getValue("Arg 1"));
        assertEquals(false, (boolean) ap.getValue("Arg 2"));
        assertEquals(true, (boolean) ap.getValue("Arg 3"));  
    }
    
    @Test
    public void testShortNamedArgumentForLongName() {
        ap.addArguments("Length", ArgumentParser.Datatype.INT, "Length of the object");
        ap.addNamedArgument("type", " ", ArgumentParser.Datatype.STRING, "t", true);
        String[] inp = {"-t", "circle", "5"};
        ap.parse(inp);
        assertEquals("circle", (String) ap.getValue("type")); 
    }
    
     @Test
    public void testAddNameArgumentWithBooleanRequired() {
        ap.addArguments("Length", ArgumentParser.Datatype.INT, "Length of the object");
        ap.addNamedArgument("type", " ", true);
        String[] inp = {"--type", "circle", "5"};
        ap.parse(inp);
        assertEquals("circle", (String) ap.getValue("type")); 
    }

    @Test
    public void testMultipleShortNamedArgumentForLongName() {
        ap.addArguments("Length", ArgumentParser.Datatype.INT, "Length of the object");
        ap.addArguments("Width", ArgumentParser.Datatype.INT, "Width of the object");
        ap.addArguments("Height", ArgumentParser.Datatype.INT, "Height of the object");
        ap.addNamedArgument("type", " ",ArgumentParser.Datatype.STRING,"t", false);
        ap.addNamedArgument("color", " ", ArgumentParser.Datatype.STRING, "c", false);
        String[] inp = {"-t", "circle", "5", "-c", "red", "7", "10"};
        ap.parse(inp);
        assertEquals("circle", (String) ap.getValue("type")); 
        assertEquals("red", (String) ap.getValue("color"));
    }
    
    @Test
    public void testOneShortNamedArgumentForLongNameAndOneLong() {
        ap.addArguments("Length", ArgumentParser.Datatype.INT, "Length of the object");
        ap.addArguments("Width", ArgumentParser.Datatype.INT, "Width of the object");
        ap.addArguments("Height", ArgumentParser.Datatype.INT, "Height of the object");
        ap.addNamedArgument("type", " ", ArgumentParser.Datatype.STRING,"t", true);
        ap.addNamedArgument("color", " ",ArgumentParser.Datatype.STRING, "c", false);
        String[] inp = {"-t", "circle", "5", "--color", "red", "7", "10"};
        ap.parse(inp);
        assertEquals("circle", (String) ap.getValue("type")); 
        assertEquals("red", (String) ap.getValue("color"));
    }
	
    @Test
    public void testDashDashFront() {
        ap.addArguments("Arg 1", ArgumentParser.Datatype.BOOLEAN, "This should be true");
        ap.addArguments("Arg 2", ArgumentParser.Datatype.BOOLEAN, "This should be false");
        ap.addArguments("Arg 3", ArgumentParser.Datatype.BOOLEAN, "This should be true");
        ap.addNamedArgument("stuff", "5", true);
        String[] inp = {"--stuff", "4", "true", "false", "true"};
        ap.parse(inp);
        assertEquals("4", (String) ap.getValue("stuff"));
    }  
    
    @Test
    public void testDashDashMiddle() {
        ap.addArguments("Arg 1", ArgumentParser.Datatype.BOOLEAN, "This should be true");
        ap.addArguments("Arg 2", ArgumentParser.Datatype.BOOLEAN, "This should be false");
        ap.addArguments("Arg 3", ArgumentParser.Datatype.BOOLEAN, "This should be true");
        ap.addNamedArgument("stuff", "5", false);
        String[] inp = { "true", "false", "--stuff", "4", "true"};
        ap.parse(inp);
        assertEquals("4", (String) ap.getValue("stuff"));
    }  
    
    @Test
    public void testDashDashEnd() {
        ap.addArguments("Arg 1", ArgumentParser.Datatype.BOOLEAN, "This should be true");
        ap.addArguments("Arg 2", ArgumentParser.Datatype.BOOLEAN, "This should be false");
        ap.addArguments("Arg 3", ArgumentParser.Datatype.BOOLEAN, "This should be true");
        ap.addNamedArgument("stuff", "5", true);
        String[] inp = {"true", "false", "true", "--stuff", "4"};
        ap.parse(inp);
        assertEquals("4", (String) ap.getValue("stuff"));
    }
    
    @Test
    public void testFlagTrueBeginning() {
        ap.addArguments("Arg 1", ArgumentParser.Datatype.BOOLEAN, "This should be true");
        ap.addFlag("flaggy");
        String[] inp = {"--flaggy", "true"};
        ap.parse(inp);
        assertEquals(true, ap.getValue("flaggy"));
    }
    
    @Test
    public void testFlagFalseBeginning() {
        ap.addFlag("flaggy");
        ap.addArguments("Arg 1", ArgumentParser.Datatype.BOOLEAN, "This should be true");
        String[] inp = {"true"};
        ap.parse(inp);
        assertEquals(false, ap.getValue("flaggy"));
    }
    
    @Test
    public void testFlagTrueMiddle() {
        ap.addArguments("Arg 1", ArgumentParser.Datatype.BOOLEAN, "Test argument as well");
        ap.addArguments("Arg 2", ArgumentParser.Datatype.BOOLEAN, "Test argument");
        ap.addFlag("flaggy");
        String[] inp = {"true","--flaggy","false"};
        ap.parse(inp);
        assertEquals(true, ap.getValue("flaggy"));
    }
    
    @Test
    public void testMultipleFlagsSeperatedBothTrue() {
        ap.addArguments("Arg 1", ArgumentParser.Datatype.BOOLEAN, "Test argument as well");
        ap.addArguments("Arg 2", ArgumentParser.Datatype.BOOLEAN, "Test argument");
        ap.addFlag("flaggy1");
        ap.addFlag("flaggy2");
        String[] inp = {"true","--flaggy1","false", "--flaggy2"};
        ap.parse(inp);
        assertEquals(true, ap.getValue("flaggy1"));
        assertEquals(true, ap.getValue("flaggy2"));
    }
    
    @Test
    public void testMultipleFlagsSeperatedOneTrueOtherFalse() {
        ap.addArguments("Arg 1", ArgumentParser.Datatype.BOOLEAN, "Test argument as well");
        ap.addArguments("Arg 2", ArgumentParser.Datatype.BOOLEAN, "Test argument");
        ap.addFlag("flaggy1");
        ap.addFlag("flaggy2");
        String[] inp = {"true","--flaggy1","false"};
        ap.parse(inp);
        assertEquals(true, ap.getValue("flaggy1"));
        assertEquals(false, ap.getValue("flaggy2"));
    }
    
    @Test
    public void testOneRequiredNamedArgument() {
        ap.addArguments("Length", ArgumentParser.Datatype.INT);
        ap.addNamedArgument("Type", true);
        String[] inp = {"7", "--Type", "sphere"};
        ap.parse(inp);
        assertEquals("sphere", ap.getValue("Type"));
    }
    
    @Test
    public void testOneRequiredNamedArgumentOneNotRequired() {
        ap.addArguments("Length", ArgumentParser.Datatype.INT);
        ap.addNamedArgument("Type", true);
        ap.addNamedArgument("Color", false);
        String[] inp = {"7", "--Type", "sphere"};
        ap.parse(inp);
        assertEquals("sphere", ap.getValue("Type"));
    }
    
    @Test
    public void testAddingThreeRestrictionsForPositionalAndUsingThemCorrectly() {
        ap.addArguments("Length", ArgumentParser.Datatype.INT);
        ap.addArguments("Width", ArgumentParser.Datatype.INT);
        ap.addArguments("Height", ArgumentParser.Datatype.INT);
        List<String> restrict = Arrays.asList("1", "2", "3");
        ap.setRestrictions("Length", restrict);
        ap.setRestrictions("Width", restrict);
        ap.setRestrictions("Height", restrict);
        String[] inp = {"1", "2", "3"};
        ap.parse(inp);
        assertEquals(1, ap.getValue("Length"));
        assertEquals(2, ap.getValue("Width"));
        assertEquals(3, ap.getValue("Height"));
    }
    
    @Test
    public void testAddingTwoRestrictionsForPositionalAndUsingThemCorrectly() {
        ap.addArguments("Length", ArgumentParser.Datatype.INT);
        ap.addArguments("Width", ArgumentParser.Datatype.INT);
        ap.addArguments("Height", ArgumentParser.Datatype.INT);
        List<String> restrict = Arrays.asList("1", "2", "3");
        ap.setRestrictions("Length", restrict);
        ap.setRestrictions("Width", restrict);
        String[] inp = {"1", "2", "6"};
        ap.parse(inp);
        assertEquals(1, ap.getValue("Length"));
        assertEquals(2, ap.getValue("Width"));
        assertEquals(6, ap.getValue("Height"));
    }
    
    @Test
    public void testAddingOneRestrictionsForNamedAndUsingThemCorrectly() {
        ap.addArguments("Length", ArgumentParser.Datatype.INT);
        ap.addArguments("Width", ArgumentParser.Datatype.INT);
        ap.addArguments("Height", ArgumentParser.Datatype.INT);
        ap.addNamedArgument("Color", "black", false);
        List<String> restrictColor = Arrays.asList("red", "yellow", "blue");
        ap.setRestrictions("Color", restrictColor);
        String[] inp = {"1", "2", "6", "--Color", "red"};
        ap.parse(inp);
        assertEquals(1, ap.getValue("Length"));
        assertEquals(2, ap.getValue("Width"));
        assertEquals(6, ap.getValue("Height"));
        assertEquals("red", ap.getValue("Color"));
    }
    
    @Test
    public void testAddingTwoRestrictionsForNamedAndUsingThemCorrectly() {
        ap.addArguments("Length", ArgumentParser.Datatype.INT);
        ap.addArguments("Width", ArgumentParser.Datatype.INT);
        ap.addArguments("Height", ArgumentParser.Datatype.INT);
        ap.addNamedArgument("Color", "black", false);
        ap.addNamedArgument("Type", true);
        List<String> restrictColor = Arrays.asList("red", "yellow", "blue");
        List<String> restrictType = Arrays.asList("sphere", "square");
        ap.setRestrictions("Color", restrictColor);
        ap.setRestrictions("Type", restrictType);
        String[] inp = {"1", "--Type", "square", "2", "6", "--Color", "red"};
        ap.parse(inp);
        assertEquals(1, ap.getValue("Length"));
        assertEquals(2, ap.getValue("Width"));
        assertEquals(6, ap.getValue("Height"));
        assertEquals("red", ap.getValue("Color"));
        assertEquals("square", ap.getValue("Type"));
    }

    
   
    @Test
    public void testCreateTwoGroupsWithTwoNamedArguments() {
        String[] inp = {"--Color", "red", "--Type", "sphere"};
        ap.addNamedArgument("Color", false);
        ap.addNamedArgument("Type", false);
        ap.addNamedArgument("Size", false);
        List<String> namedArgs1 = new ArrayList<>();
        List<String> namedArgs2 = new ArrayList<>();
        namedArgs1.add("Color");
        namedArgs1.add("Type");
        namedArgs2.add("Size");
        List<List<String>> listOfNamed= new ArrayList<>();
        listOfNamed.add(namedArgs1);
        listOfNamed.add(namedArgs2);
        ap.addNamedGroups(listOfNamed);
        ap.parse(inp);
        assertEquals("red", ap.getValue("Color"));
        assertEquals("sphere", ap.getValue("Type"));
    }
    
    @Test
    public void testGettingListOfValuesAsFloat() {
        String[] inp = {"6.3", "3.1", "5.2", "9.1"};
        ap.addArguments("floats", ArgumentParser.Datatype.FLOAT, "Flaot Stuff", 4);
        ap.parse(inp);
        List<Float> floatList = ap.getValue("floats");
        assertEquals(6.3f, floatList.get(0).floatValue(), 0.1);
        assertEquals(3.1f, floatList.get(1).floatValue(), 0.1);
        assertEquals(5.2f, floatList.get(2).floatValue(), 0.1);
        assertEquals(9.1f, floatList.get(3).floatValue(), 0.1);
    }
    
    @Test
    public void testCreateThreeGroupsWithFiveNamedArguments() {
        String[] inp = {"--Color", "red", "--Type", "sphere"};
        ap.addNamedArgument("Color", false);
        ap.addNamedArgument("Type", false);
        ap.addNamedArgument("Size", false);
        ap.addNamedArgument("Shape", false);
        ap.addNamedArgument("Weight", false);
        ap.addNamedArgument("Smell", false);
        List<String> namedArgs1 = new ArrayList<>();
        List<String> namedArgs2 = new ArrayList<>();
        List<String> namedArgs3 = new ArrayList<>();
        namedArgs1.add("Color");
        namedArgs1.add("Type");
        namedArgs2.add("Size");
        namedArgs2.add("Shape");
        namedArgs3.add("Weight");
        namedArgs3.add("Smell");
        List<List<String>> listOfNamed= new ArrayList<>();
        listOfNamed.add(namedArgs1);
        listOfNamed.add(namedArgs2);
        listOfNamed.add(namedArgs3);
        ap.addNamedGroups(listOfNamed);
        ap.parse(inp);
        assertEquals("red", ap.getValue("Color"));
        assertEquals("sphere", ap.getValue("Type"));
    }
    
    @Test (expected = MutualExclusionException.class)
    public void testCreateThreeGroupsWithFiveNamedArgumentsThrowException() {
        String[] inp = {"--Color", "red", "--Type", "sphere", "--Smell", "roses"};
        ap.addNamedArgument("Color", false);
        ap.addNamedArgument("Type", false);
        ap.addNamedArgument("Size", false);
        ap.addNamedArgument("Shape", false);
        ap.addNamedArgument("Weight", false);
        ap.addNamedArgument("Smell", false);
        List<String> namedArgs1 = new ArrayList<>();
        List<String> namedArgs2 = new ArrayList<>();
        List<String> namedArgs3 = new ArrayList<>();
        namedArgs1.add("Color");
        namedArgs1.add("Type");
        namedArgs2.add("Size");
        namedArgs2.add("Shape");
        namedArgs3.add("Weight");
        namedArgs3.add("Smell");
        List<List<String>> listOfNamed= new ArrayList<>();
        listOfNamed.add(namedArgs1);
        listOfNamed.add(namedArgs2);
        listOfNamed.add(namedArgs3);
        ap.addNamedGroups(listOfNamed);
        ap.parse(inp);
        assertEquals("red", ap.getValue("Color"));
        assertEquals("sphere", ap.getValue("Type"));
    }
    
    @Test (expected = MutualExclusionException.class)
    public void testCreateTwoGroupsWithTwoNamedArgumentsThrowException() {
        String[] inp = {"--Color", "red", "--Size", "sphere"};
        ap.addNamedArgument("Color", false);
        ap.addNamedArgument("Type", false);
        ap.addNamedArgument("Size", false);
        List<String> namedArgs1 = new ArrayList<>();
        List<String> namedArgs2 = new ArrayList<>();
        namedArgs1.add("Color");
        namedArgs1.add("Type");
        namedArgs2.add("Size");
        List<List<String>> listOfNamed= new ArrayList<>();
        listOfNamed.add(namedArgs1);
        listOfNamed.add(namedArgs2);
        ap.addNamedGroups(listOfNamed);
        ap.parse(inp);
    }
    
    @Test
    public void testMultipleValuesForOnePositional() {
        String[] inp = {"5", "6", "7"};
        ap.addArguments("Dimensions", ArgumentParser.Datatype.INT, "Cube size", 3);
        ap.parse(inp);
        List<Integer> values;
        values = ap.getValue("Dimensions");
        assertEquals(new Integer(5), values.get(0));
        assertEquals(new Integer(6), values.get(1));
        assertEquals(new Integer(7), values.get(2));
    }
    
    @Test
    public void testMultipleValuesForOnePositionalBoolean() {
        String[] inp = {"true", "false"};
        ap.addArguments("Thing", ArgumentParser.Datatype.BOOLEAN, "stuff", 2);
        ap.parse(inp);
        List<Integer> values;
        values = ap.getValue("Thing");
        assertEquals(new Boolean(true), values.get(0));
        assertEquals(new Boolean(false), values.get(1));
    }
    
    @Test
    public void testMultipleValuesForOnePositionalString() {
        String[] inp = {"dog", "cat"};
        ap.addArguments("Pets", ArgumentParser.Datatype.STRING, "pets owned", 2);
        ap.parse(inp);
        List<Integer> values;
        values = ap.getValue("Pets");
        assertEquals(new String("dog"), values.get(0));
        assertEquals(new String("cat"), values.get(1));
    }
    
    @Test (expected = PositionalArgumentException.class)
    public void testMultipleValuesNotEnoughGivenException() {
        String[] inp = {"5", "6"};
        ap.addArguments("Dimensions", ArgumentParser.Datatype.INT, "Cube size", 3);
        ap.parse(inp);
        List<Integer> values;
        values = ap.getValue("Dimensions");
        assertEquals(new Integer(5), values.get(0));
        assertEquals(new Integer(6), values.get(1));
    }
    
    @Test (expected = PositionalArgumentException.class)
    public void testMultipleValuesAtEndNotEnoughGivenException() {
        String[] inp = {"Red", "5", "6"};
        ap.addArguments("Color", ArgumentParser.Datatype.STRING);
        ap.addArguments("Dimensions", ArgumentParser.Datatype.INT, "Cube size", 3);
        ap.parse(inp);
    }
    
    @Test (expected = PositionalArgumentException.class)
    public void testMultipleValuesAtBeginNotEnoughGivenException() {
        String[] inp = {"5", "6", "7"};
        ap.addArguments("Dimensions", ArgumentParser.Datatype.INT, "Cube size", 3);
        ap.addArguments("Cube Size", ArgumentParser.Datatype.INT);
        ap.parse(inp);
    }
    
    @Test(expected = InvalidDataTypeException.class)
    public void testEnterFloatWhenShouldBeInt() {
        ap.addArguments("Arg 1", ArgumentParser.Datatype.INT);
        String[] inp = {"1.5"};
        ap.parse(inp);
    }
    
    @Test(expected = InvalidDataTypeException.class)
    public void testEnterIntWhenShouldBeBoolean() {
        ap.addArguments("Arg 1", ArgumentParser.Datatype.BOOLEAN);
        String[] inp = {"1"};
        ap.parse(inp);
    }
    
    @Test(expected = InvalidDataTypeException.class)
    public void testEnterStringWhenShouldBeFloat() {
        ap.addArguments("Arg 1", ArgumentParser.Datatype.FLOAT);
        String[] inp = {"hello"};
        ap.parse(inp);
    }
    
    @Test(expected = PositionalArgumentException.class)
    public void testUserEntersTooManyPositionalArguments() {
        ap.addArguments("Arg 1", ArgumentParser.Datatype.INT);
        String[] inp = {"1", "2"};
        ap.parse(inp);
    }
    
    @Test(expected = PositionalArgumentException.class)
    public void testNotEnoughPositionalArgsGiven() {
        ap.addArguments("Arg 1", ArgumentParser.Datatype.INT);
        ap.addArguments("Arg2", ArgumentParser.Datatype.INT);
        String[] inp = {"1"};
        ap.parse(inp);
    }
    
    @Test(expected = NoArgCalledException.class)
    public void testProductOwnerCallsForValueThatIsNotAnArgument() {
        ap.addArguments("Length", ArgumentParser.Datatype.INT);
        String[] inp = {"5"};
        ap.parse(inp);         
        ap.getValue("Width");
    }
    
    @Test (expected = InvalidNamedArgumentException.class)
    public void testUserEnterInvalidLongArgument() {
        ap.addNamedArgument("type", false);
        String[] inp = {"--circle"};
        ap.parse(inp);
    }
    
    @Test (expected = InvalidNamedArgumentException.class)
    public void testUserEnterInvalidShortArgument() {
        ap.addNamedArgument("type", "",ArgumentParser.Datatype.STRING, "t", false);
        String[] inp = {"-c"};
        ap.parse(inp);
    }
    
    @Test (expected = RequiredNamedArgumentNotGivenException.class)
    public void testRequiredNamedArgumentNotGiven() {
        ap.addNamedArgument("Type", "", ArgumentParser.Datatype.STRING, "t", true);
        ap.addArguments("Length", ArgumentParser.Datatype.INT);
        String[] inp = {"7"};
        ap.parse(inp);
    }
    
    @Test (expected = RestrictedValueException.class)
    public void testRestrictionPositionalException() {
        ap.addArguments("Color", ArgumentParser.Datatype.STRING);
        List<String> restrictColor = Arrays.asList("red", "green", "blue");
        ap.setRestrictions("Color", restrictColor);
        String[] inp = {"yellow"};
        ap.parse(inp);
    }

    
    @Test (expected = RestrictedValueException.class)
    public void testRestrictionLongNamedException() {
        ap.addArguments("Length", ArgumentParser.Datatype.INT);
        ap.addNamedArgument("Color", true);
        List<String> restrictColor = Arrays.asList("red", "green", "blue");
        ap.setRestrictions("Color", restrictColor);
        String[] inp = {"1", "--Color", "yellow"};
        ap.parse(inp);
    }
    
    @Test (expected = RestrictedValueException.class)
    public void testRestrictionShortNamedException() {
        ap.addArguments("Length", ArgumentParser.Datatype.INT);
        ap.addNamedArgument("Color", "", ArgumentParser.Datatype.STRING, "c", true);
        List<String> restrictColor = Arrays.asList("red", "green", "blue");
        ap.setRestrictions("Color", restrictColor);
        String[] inp = {"1", "-c", "yellow"};
        ap.parse(inp);
    }
    
    @Test
    public void testHelp(){
        ap.addArguments("pet", ArgumentParser.Datatype.STRING);
        ap.addArguments("number", ArgumentParser.Datatype.INT, "some number");
        ap.addArguments("rainy", ArgumentParser.Datatype.BOOLEAN, "is it rainy");
        ap.addArguments("bathrooms", ArgumentParser.Datatype.FLOAT, "Other thing");
        ap.addArguments("Length", ArgumentParser.Datatype.INT, "Length of the object");
        ap.addArguments("Width", ArgumentParser.Datatype.INT, "Width of the object");
        ap.addArguments("Height", ArgumentParser.Datatype.INT, "Height of the object");
        List<String> petRestrict = Arrays.asList("dog", "cat", "bird");
        ap.setRestrictions("pet", petRestrict);
        ap.addNamedArgument("Type", " ", ArgumentParser.Datatype.STRING, "t", true);
        ap.addNamedArgument("Color", true);
        ap.addNamedArgument("G1Test", false);
        ap.addNamedArgument("G1Test2", false);
        ap.addNamedArgument("G1Test3", false);
        ap.addNamedArgument("G2Test", false);
        ap.addNamedArgument("G2Test2", false);
        ap.addNamedArgument("G2Test3", false);
        List<String> names1 = new ArrayList<String>();
        names1.add("G1Test"); 
        names1.add("G1Test2"); 
        names1.add("G1Test3");
        List<String> names2 = new ArrayList<String>();
        names2.add("G2Test"); 
        names2.add("G2Test2"); 
        names2.add("G2Test3");
        List<List<String>> groups = new ArrayList<List<String>>();
        groups.add(names1); 
        groups.add(names2);
        ap.addNamedGroups(groups);
        ap.addFlag("x");
        ap.addFlag("w");
        ap.setProgramDescription("This description can be edited");
        ap.setProgramName("VolumeCalculator");
        ap.getHelpInfo();
    }

}
 
  

