# How to Create a Command for ClapBot

## Introduction

A 'command' is defined as any class that inherits from `GenericCommand<E>`,
where `E` is the JDA Event that the command handles. A command can handle any
JDA event, but most of the time you will be creating a command that just
handles messages being sent, i.e. `MessageReceievedEvent`.

## Setting Up

Create a class for the command in an appropriate package inside
`joecord.seal.clapbot.commands`, it's convention to end the class name with
`Command`, eg. `EchoCommand`.

The class needs to extend `GenericCommand<E>`, with `E` replaced with the
appropriate event.

You need to define a constructor for the command, and inside you must call
the super constructor with two things; The Event Class that you are handling,
and and 'Command Properties' that you need. (We will get to those later).

You also should change the values of `displayName` and `description`, which
are fields stored in `GenericCommand`. These are just for show.

Also remember to register your command in `ClapBot.java`.

Eg.

```java
public class MyNewCommand extends GenericCommand<MessageReceievedEvent> {
    public MyNewCommand() {
        super(MessageReceievedEvent.class, /* Command Properties */);

        this.displayName = "My New Command";
        this.description = "Does stuff!";
    }
}
```

Next, you need to override the `execute` method. This is called whenever the
event that the command handles is received, and the event itself is given
as a parameter in `execute`.

Eg.

```java
public class MyNewCommand extends GenericCommand<MessageReceievedEvent> {
    public MyNewCommand() { ... }

    @Override
    public void execute(MessageReceivedEvent event) {
        event.getChannel().sendMessage("Hello").queue();
    }
}
```

This example command will now send `"Hello"` in response to every message sent.

## Command Properties

A command has zero or more properties, which are used to specify the
functionaliy and features of the command. They are implemented using the enum
`CommandProperty`, and are given in the command's call to the super constructor
after giving the event class.

Eg. For the command properties `INVOKED` and `USES_ARGUMENTS`:

```java
public MyNewCommand() {
    super(MessageReceievedEvent.class, CommandProperty.INVOKED, CommandProperty.USES_ARGUMENTS);

    this.displayName = "My New Command";
    this.description = "Does stuff!";
}
```

### INVOKED

This specifies that the command is to be executed when it is explicitly called
in a message along with the ClapBot prefix. These commands have one or more
aliases which must be specified.

Commands with this property must set their aliases at construction time using
`setAliases()`.

Eg.

```java
public class MyNewCommand extends GenericCommand<MessageReceievedEvent> {
    public MyNewCommand() {
        super(MessageReceievedEvent.class, CommandPropery.INVOKED);

        super.setAliases("mynewcommand", "mynewcmd");
        // With prefix "!", this command could be executed with "!mynewcommand"
     }

    @Override
    public void execute(MessageReceivedEvent event) {
        ...
    }
}
```

### USES_ARGUMENTS

This property requires the property INVOKED to be present. This specifies that
the invoked command also uses arguments given after the alias.

Commands with this property can get their aliases at any time outside of
construction time with `getArguments()`. These commands can also optionally
call `setArgumentsDesc()` at construction time to set a string describing the
syntax of the command.

Eg.

```java
public class MyEchoCommand extends GenericCommand<MessageReceievedEvent> {
    public MyEchoCommand() {
        super(MessageReceievedEvent.class, CommandPropery.INVOKED, CommandProperty.USES_ARGUMENTS);

        super.setAliases("myechocmd");
        // With prefix "!", this command could be executed with "!myechocmd"

        // Optional
        super.setArgumentsDesc("myechcocmd <message to repeat>");
     }

    @Override
    public void execute(MessageReceivedEvent event) {
        event.getChannel().sendMessage(Arrays.toString(super.getArguments())).queue();
        // For prefix "!", sending "!myechocmd 1 2 3 would produce "[1, 2, 3]"
    }
}
```

### CONDITIONAL

This specifies that the command has a condition that is checked before calling
`execute()`, if the condition fails (returns false), then `execute()` is not
called.

Commands with this property must set their condition at construction time with
`setCondition()`, which takes in a predicate that takes in the event. If you
are not comfortable using predicates, the example below shows how you can
easily set this up using a local method that contains the logic. These commands
can also optionally call `setConditionDesc()` at construction time to set a
string describing the check.

Eg.

```java
public class NotACult extends GenericCommand<MessageReceievedEvent> {
    public NotACult() {
        super(MessageReceievedEvent.class, CommandPropery.CONDITIONAL);

        // Using a local method with the check logic
        super.setCondition(event -> privateConditionCheck(event));

        // Using normal predicate syntax
        super.setCondition(event -> event.getMessage().getContentRaw().contains("cult"));

        // Optional
        super.setConditionDesc("Checks if the message contains 'cult'");
     }

    @Override
    public void execute(MessageReceivedEvent event) {
        // This will only execute if the condition returned true
        event.getChannel().sendMessage("Not a cult").queue();
    }

    // Contains the condition logic if you prefer
    private boolean privateConditionCheck(MessageReceivedEvent event) {
        return event.getMessage().getContentRaw().contains("cult");
    }
}
```

### PRIVELAGED

This is functionally identical to CONDITIONAL, it is just an extra check that
is done before the condition check and is conventionally used to check the user
that the event concerns.

Commands with this property must set their privelage check at construction time
with `setPrivelage()`, which takes in a predicate that takes in the event, see
CONDITIONAL for more details. These commands can also optionally call
`setPrivelageDesc()` at construction time to set a string describing the check.

Eg.

```java
public class PrivelageExample extends GenericCommand<MessageReceievedEvent> {
    public PrivelageExample() {
        super(MessageReceievedEvent.class, CommandPropery.PRIVELAGED);

        // Using a local method with the check logic
        super.setPrivelage(event -> privatePrivelageCheck(event));

        // Using normal predicate syntax
        super.setPrivelage(event -> event.getAuthor().getIdLong() == 297978513250713602l);

        // Optional
        super.setPrivelageDesc("Checks if the message is sent by alec");
     }

    @Override
    public void execute(MessageReceivedEvent event) {
        // This will only execute if the privelage check returned true
        event.getChannel().sendMessage("Hi alec").queue();
    }

    // Contains the privelage check logic if you prefer
    private boolean privatePrivelageCheck(MessageReceivedEvent event) {
        return event.getAuthor().getIdLong() == 297978513250713602l;
    }
}
```

### META

This specifies that the command requires access to ClapBot's command handler.

Commands with this property can call `getCommandHandler()` at any time to get
it.

Eg.

```java
public class PrefixChecker extends GenericCommand<MessageReceievedEvent> {
    public PrefixChecker() {
        super(MessageReceievedEvent.class, CommandProperty.INVOKED, CommandProperty.META);

        // INVOKED stuff
        super.setArguments("whatstheprefix");
    }

    @Override
    public void execute(MessageReceivedEvent event) {
        String prefix = super.getCommandHandler().getPrefix();
        event.getChannel().sendMessage("It's " + prefix).queue();
    }
}
```

### RESPECT_BOTS

This specifies that the command will still be executed if the user that concerns
the event is a bot. Many JDA events do not concern a user and for those this
property has no effect.

Eg.

```java
public class LoggerCommand extends GenericCommand<MessageReceievedEvent> {
    public LoggerCommand() {
        super(MessageReceievedEvent.class, CommandProperty.RESPECT_BOTS);
    }

    @Override
    public void execute(MessageReceivedEvent event) {
        System.out.println(
            event.getUser().getName() + " just said " + event.getMessage().getContentRaw());
    }
}
```
