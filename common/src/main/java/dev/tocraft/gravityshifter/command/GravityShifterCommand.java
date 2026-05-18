package dev.tocraft.gravityshifter.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.tree.LiteralCommandNode;
import dev.tocraft.gravityshifter.GravityShifter;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;

import java.util.Collection;

public class GravityShifterCommand implements dev.tocraft.craftedcore.event.common.CommandEvents.CommandRegistration {
    @Override
    public void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext registry, Commands.CommandSelection selection) {
        LiteralCommandNode<CommandSourceStack> rootNode = Commands.literal(GravityShifter.MODID)
                .build();

        rootNode.addChild(buildResetCommand());
        rootNode.addChild(buildSetCommand());
        rootNode.addChild(buildGetCommand());
        rootNode.addChild(buildDefaultCommand());

        dispatcher.getRoot().addChild(rootNode);
    }

    private static LiteralCommandNode<CommandSourceStack> buildDefaultCommand() {
        return Commands.literal("default").then(
                        Commands.literal("get")
                                .executes(context -> {
                                    Direction current = Direction.DOWN;
                                    context.getSource().sendSuccess(
                                            () -> Component.translatable("gravityshifter.command.default.get", current), true);
                                    return 1;
                                })).then(
                Commands.literal("reset")
                        .executes(context -> {
                            context.getSource().sendSuccess(
                                    () -> Component.translatable("gravityshifter.command.default.reset"), true);
                            return 1;
                        })
        )
                .then(Commands.literal("set")
                        .then(Commands.argument("direction", StringArgumentType.string())
                                .suggests((ctx, builder) -> {
                                    for (Direction dir : Direction.values()) {
                                        builder.suggest(dir.getName());
                                    }
                                    return builder.buildFuture();
                                })
                                .executes(context -> {
                                    String dir = StringArgumentType.getString(context, "direction");
                                    Direction direction = Direction.byName(dir);
                                    if (direction != null) {
                                        context.getSource().sendSuccess(
                                                () -> Component.translatable("gravityshifter.command.default.set.success", direction.getName()), true);
                                        return 1;
                                    } else {
                                        context.getSource().sendFailure(Component.translatable("gravityshifter.command.direction.failed", dir));
                                        return 0;
                                    }
                                }))).build();
    }

    private static LiteralCommandNode<CommandSourceStack> buildGetCommand() {
        return Commands.literal("reset")
                .then(Commands.argument("entities", EntityArgument.entities())
                        .executes(context -> {
                            Collection<? extends Entity> entities = EntityArgument.getEntities(context, "entities");
                            for (Entity entity : entities) {
                                Direction current = Direction.DOWN;
                                context.getSource().sendSuccess(
                                        () -> Component.translatable("gravityshifter.command.get", entity.getDisplayName(), current), true);
                                return 1;
                            }
                            return 1;
                        }))
                .build();
    }

    private static LiteralCommandNode<CommandSourceStack> buildResetCommand() {
        return Commands.literal("reset")
                .then(Commands.argument("entities", EntityArgument.entities())
                        .executes(context -> {
                            Collection<? extends Entity> entities = EntityArgument.getEntities(context, "entities");
                            for (Entity entity : entities) {
                                context.getSource().sendSuccess(
                                        () -> Component.translatable("gravityshifter.command.reset", entity.getDisplayName()), true);
                            }
                            return 1;
                        }))
                .build();
    }

    private static LiteralCommandNode<CommandSourceStack> buildSetCommand() {
        return Commands.literal("set")
                .then(Commands.argument("entities", EntityArgument.entities())
                        .then(Commands.argument("direction", StringArgumentType.string())
                                .suggests((ctx, builder) -> {
                                    for (Direction dir : Direction.values()) {
                                        builder.suggest(dir.getName());
                                    }
                                    return builder.buildFuture();
                                })
                                .executes(context -> {
                                    String dir = StringArgumentType.getString(context, "direction");
                                    Direction direction = Direction.byName(dir);
                                    if (direction != null) {
                                        Collection<? extends Entity> entities = EntityArgument.getEntities(context, "entities");
                                        for (Entity entity : entities) {
                                            context.getSource().sendSuccess(
                                                    () -> Component.translatable("gravityshifter.command.set.success", entity.getDisplayName(), direction.getName()), true);
                                        }
                                        return 1;
                                    } else {
                                        context.getSource().sendFailure(Component.translatable("gravityshifter.command.direction.failed", dir));
                                        return 0;
                                    }
                                })))
                .build();
    }
}
