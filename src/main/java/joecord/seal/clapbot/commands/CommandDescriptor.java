package joecord.seal.clapbot.commands;

public class CommandDescriptor<T extends GenericCommand> implements GenericCommand {
    
    Class<T> commandClass;
    public String NAME = "";
    public String DESCRIPTION = "";
    public String[] ALIASES = new String[0];
    public String USAGE = "";

    public CommandDescriptor(Class<T> commandClass) {
        this.commandClass = commandClass;

        try {
            this.NAME = (String)commandClass.getField("NAME").get(null);
            this.DESCRIPTION = (String)commandClass.getField("DESCRIPTION").get(null);
            this.USAGE = (String)commandClass.getField("USAGE").get(null);
            this.ALIASES = (String[])commandClass.getField("ALIASES").get(null);
        }
        catch(IllegalAccessException | NoSuchFieldException e) {}
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
}