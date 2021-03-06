import java.util.*;
import java.io.*;
import java.util.logging.*;

public class Menu {
/* Creates a hash map to store components in.
   Calls methods based on the menu option the user chooses.
   Output is directed to the console unless the "save" option is chosen, in which case it's directed to a file the user names.
 */
    public static void main(String[] args) {
        final Logger LOGGER = Logger.getLogger(Menu.class.getName());
        Date date = new Date();
        LOGGER.info("Main method invoked at " + date.toString());
        //This hash map will hold all the components the user adds
        HashMap<String, AddComponents> components = new HashMap<String, AddComponents>();
        while (true) {
            Scanner sc = new Scanner(System.in);
            //PrintStream std = System.out;
            //PrintStream fileStream;
            System.out.println("Type \"setup\" to set up your account for the first time");
            System.out.println("Type \"list\" for a list of all saved components");
            System.out.println("Type \"add\" to add a new component");
            //System.out.println("Type the name of a component to view or edit its details");
            System.out.println("Type \"details\" to view details for a component");
            System.out.println("Type \"save\" to write this info to a file.");
            System.out.println("Type \"exit\" to exit the program");
            String response = sc.next().toLowerCase();
            date = new Date();
            LOGGER.info(response + " option invoked at " + date.toString());
            if (response.equals("setup")) {
                setup(components, sc);
            } else if (response.equals("list"))
                if (components.isEmpty()) {
                    System.out.println("There are no saved items yet!");
                } else {
                    listComponents(components);
                }
            else if (response.equals("add")) {
                System.out.println("Enter the name of the item: ");
                sc.next();
                String name = sc.nextLine();
                System.out.println("Enter the model of the item or n/a if not available: ");
                String model = sc.nextLine();
                System.out.println("Enter the date you last serviced the item: ");
                String lastChangeDate = sc.nextLine();
                System.out.println("Enter your notes/instructions for this item: ");
                String notes = sc.nextLine();
                componentFactory(components, name, model, lastChangeDate, notes);
            }
            else if (response.equals("details")) {
                if (components.isEmpty())
                    System.out.println("There are no saved items yet!");
                else {
                    System.out.println("Type the name of the item that you'd like to view:");
                    sc.nextLine();
                    String response2 = sc.nextLine();
                    while (!components.containsKey(response2)) {
                        System.out.println("That item is not in the list! Please enter the name of the item you wish to view:");
                        response2 = sc.nextLine();
                    }
                    getComponentInfo(response2, components);
                }
            } else if (response.equals("save")) {
                System.out.println("Enter your name:");
                sc.nextLine();
                String response3 = (sc.nextLine());
                String filename = response3 + ".txt";
                //Stackoverflow question 2885173
                try {
                    PrintWriter writer = new PrintWriter(filename, "UTF-8");
                    writer.println(response3 + "'s Home List");
                    for (String key : components.keySet())
                        writer.println(saveComponentInfo(key, components));
                    writer.close();
                    System.out.println("Your file has been saved!");
                } catch (UnsupportedEncodingException e){
                    date = new Date();
                    LOGGER.info(e.getMessage() + " " + date.toString());
                    System.out.println(e.getMessage());
                } catch (FileNotFoundException e) {
                    date = new Date();
                    LOGGER.info(e.getMessage() + " " + date.toString());
                }
            }
            else if (response.equals("exit")){
                System.exit(0);
            }else
                System.out.println("What you've entered is not one of the menu options");
        }
    }

    //Walks the user through adding a heater, light bulbs, and windows to the component list, calling each component's custom constructor
    public static void setup(HashMap<String, AddComponents> components, Scanner sc){
        String notes = "n/a";
        System.out.println("First we'll set up info about your heater");
        System.out.println("What is the model of your heater? Type n/a if not applicable or available.");
        sc.next();
        String model = sc.nextLine();
        System.out.println("What is the date you last serviced this item?");
        String lastChangeDate = sc.nextLine();
        componentFactory(components,"heater", model, lastChangeDate, notes);
        System.out.println("Next we'll set up info about your light bulbs");
        System.out.println("What type of bulbs do you use? Type n/a if not applicable or available.");
        model = sc.nextLine();
        System.out.println("When did you last replace your light bulbs? Type n/a if you are not sure.");
        lastChangeDate = sc.nextLine();
        componentFactory(components,"light bulbs", model, lastChangeDate, notes);
        System.out.println("Lastly we'll set up info about your windows");
        System.out.println("What type of windows do you have? Type n/a if not applicable or available.");
        model = sc.nextLine();
        System.out.println("When did you last perform maintenance on your windows? Type n/a if not applicable or available.");
        lastChangeDate = sc.nextLine();
        componentFactory(components,"windows", model, lastChangeDate, notes);
    }

    //Lists the names of all components
    public static void listComponents(HashMap<String, AddComponents> components) {
        System.out.println("My home maintenance items:");
        for (String key : components.keySet())
            System.out.println(key);
    }

    //Creates a new component and adds it to the component list
    public static void componentFactory(HashMap<String, AddComponents> components, String name, String model, String lastChangeDate, String notes){
        AddComponents component;
        if (name.equals("heater"))
            component = new Heater(model, lastChangeDate);
        else if (name.equals("light bulbs"))
            component = new LightBulbs(model, lastChangeDate);
        else if (name.equals("windows"))
            component = new Windows(model, lastChangeDate);
        else
            component = new AddComponents(name, model, lastChangeDate, notes);
        components.put(component.getItemName(), component);
        System.out.println("Added item to component list");
    }

    //Edits a component or creates a new one and adds it to the component list if it doesn't exist
    //Used when component name is known
    public static void componentEdit(HashMap<String, AddComponents> components, String name){
        Scanner sc = new Scanner(System.in);
        AddComponents component = components.get(name);
        System.out.println("Type \"name\" to edit the name");
        System.out.println("Type \"model\" to edit the model");
        System.out.println("Type \"date\" to edit the date of the last maintenance");
        System.out.println("Type \"notes\" to edit the notes");
        String response = sc.next();
        if (response.equals("name")) {
            System.out.println("Enter the new name for this item: ");
            String newName = sc.nextLine();
            component.setItemName(newName);
        }
        else if (response.equals("model")) {
            System.out.println("Enter the new model for this item: ");
            String newModel = sc.nextLine();
            component.setModel(newModel);
        }
        else if (response.equals("date")) {
            System.out.println("Enter the date you last serviced this item: ");
            String newDate = sc.nextLine();
            component.setLastChangeDate(newDate);
        }
        else if (response.equals("notes")) {
            System.out.println("Enter your updated notes for this item: ");
            String newNotes = sc.nextLine();
            component.setNotes(newNotes);
        }
        else
            System.out.println("That was not one of the options, returning to the main menu..");
        components.put(component.getItemName(), component);
        System.out.println("Added updates to component list");
    }

    //Prints out a component's info
    //Asks if the user wants to edit the component and calls a component editing method if yes
    public static void getComponentInfo(String name, HashMap<String, AddComponents> components) {
        Scanner sc = new Scanner(System.in);
        AddComponents component = components.get(name);
        System.out.println("Name: " + component.getItemName());
        System.out.println("Model: " + component.getModel());
        System.out.println("Date of last maintenance: " + component.getLastChangeDate());
        System.out.println("Maintenance notes: " + component.getNotes());
        System.out.println("Would you like to edit this component? (y/n)");
        String response = sc.next().toLowerCase();
        if ((response.equals("y")) || (response.equals("Y")))
            componentEdit(components, name);
    }

    public static String saveComponentInfo(String name, HashMap<String, AddComponents> components) {
        AddComponents component = components.get(name);
        return "Name: " + component.getItemName()
                + " Model: " + component.getModel()
                + " Date of last maintenance: " + component.getLastChangeDate()
                + " Maintenance notes: " + component.getNotes();
    }
}