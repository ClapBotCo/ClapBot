package joecord.seal.clapbot.commands;

public class CommandDescriptor<T extends GenericCommand> implements GenericCommand {
    
    Class<T> commandClass;

    public CommandDescriptor(Class<T> commandClass) {
        this.commandClass = commandClass;
    }

    public T getInstance(Class<?>[] initParamClasses, Object[] initParams) {
        try {
            return commandClass
                .getConstructor(initParamClasses)
                .newInstance(initParams);
        }
        catch(ReflectiveOperationException e) {
            return null;
        }
    }

    @Override
    public void execute() {
        // Do nothing
    }

    @Override
    public void execute(String[] arguments) {
        // Do nothing
    }

    @Override
    public String getName() {
        try {
            return (String)commandClass.getField("name").get(null);
        }
        catch(IllegalAccessException | NoSuchFieldException e) {
            return "";
        }
    }

    @Override
    public String getDescription() {
        try {
            return (String)commandClass.getField("description").get(null);
        }
        catch(IllegalAccessException | NoSuchFieldException e) {
            return "";
        }
    }

    public String[] getAliases() {
        try {
            return (String[])commandClass.getField("aliases").get(null);
        }
        catch(IllegalAccessException | NoSuchFieldException e) {
            return new String[0];
        }
    }
}