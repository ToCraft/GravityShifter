package dev.tocraft.gravityshifter.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.tree.LiteralCommandNode;
import dev.tocraft.gravityshifter.GravityShifter;
import dev.tocraft.gravityshifter.api.GravityData;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

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
                                    ServerLevel level = context.getSource().getLevel();
                                    Direction gravity = GravityData.getGravity(level);
                                    context.getSource().sendSuccess(
                                            () -> Component.translatable("gravityshifter.command.default.get", gravity.getName()), true);
                                    return 1;
                                })).then(
                Commands.literal("reset")
                        .executes(context -> {
                            ServerLevel level = context.getSource().getLevel();
                            GravityData.setGravity(level, Direction.DOWN);
                            context.getSource().sendSuccess(
                                    () -> Component.translatable("gravityshifter.command.default.reset"), true);
                            return 1;
                        })
        )
                .then(Commands.literal("set")
                        .then(Commands.argument("direction", StringArgumentType.string())
                                .suggests((_, builder) -> {
                                    for (Direction dir : Direction.values()) {
                                        builder.suggest(dir.getName());
                                    }
                                    return builder.buildFuture();
                                })
                                .executes(context -> {
                                    String dir = StringArgumentType.getString(context, "direction");
                                    Direction direction = Direction.byName(dir);
                                    if (direction != null) {
                                        ServerLevel level = context.getSource().getLevel();
                                        GravityData.setGravity(level, direction);
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
        return Commands.literal("get")
                .then(Commands.argument("entities", EntityArgument.entities())
                        .executes(context -> {
                            Collection<? extends Entity> entities = EntityArgument.getEntities(context, "entities");
                            for (Entity entity : entities) {
                                if (entity instanceof LivingEntity livingEntity) {
                                    Direction current =  GravityData.getGravity(livingEntity);
                                    context.getSource().sendSuccess(
                                            () -> Component.translatable("gravityshifter.command.get", entity.getDisplayName(), current.getName()), true);
                                } else {
                                    context.getSource().sendFailure(Component.translatable("gravityshifter.command.not_living", entity.getDisplayName()));
                                }
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
                                if (entity instanceof LivingEntity livingEntity) {
                                    GravityData.setGravity(livingEntity, Direction.DOWN);
                                    context.getSource().sendSuccess(
                                            () -> Component.translatable("gravityshifter.command.reset", entity.getDisplayName()), true);
                                } else {
                                    context.getSource().sendFailure(Component.translatable("gravityshifter.command.not_living", entity.getDisplayName()));
                                }
                            }
                            return 1;
                        }))
                .build();
    }

    private static LiteralCommandNode<CommandSourceStack> buildSetCommand() {
        return Commands.literal("set")
                .then(Commands.argument("entities", EntityArgument.entities())
                        .then(Commands.argument("direction", StringArgumentType.string())
                                .suggests((_, builder) -> {
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
                                            if (entity instanceof LivingEntity livingEntity) {
                                                GravityData.setGravity(livingEntity, direction);
                                                context.getSource().sendSuccess(
                                                        () -> Component.translatable("gravityshifter.command.set.success", entity.getDisplayName(), direction.getName()), true);
                                            } else {
                                                context.getSource().sendFailure(Component.translatable("gravityshifter.command.not_living", entity.getDisplayName()));
                                            }
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
