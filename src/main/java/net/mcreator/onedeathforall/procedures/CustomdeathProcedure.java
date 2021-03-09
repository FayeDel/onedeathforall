package net.mcreator.onedeathforall.procedures;

import net.minecraftforge.fml.server.ServerLifecycleHooks;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.common.MinecraftForge;

import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.World;
import net.minecraft.world.IWorld;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.DamageSource;
import net.minecraft.server.MinecraftServer;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.ScoreCriteria;
import net.minecraft.scoreboard.Score;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.effect.LightningBoltEntity;
import net.minecraft.entity.Entity;

import net.mcreator.onedeathforall.OnedeathforallModElements;

import java.util.Map;
import java.util.List;
import java.util.HashMap;

@OnedeathforallModElements.ModElement.Tag
public class CustomdeathProcedure extends OnedeathforallModElements.ModElement {
	public CustomdeathProcedure(OnedeathforallModElements instance) {
		super(instance, 1);
		MinecraftForge.EVENT_BUS.register(this);
	}

	public static void executeProcedure(Map<String, Object> dependencies) {
		if (dependencies.get("entity") == null) {
			if (!dependencies.containsKey("entity"))
				System.err.println("Failed to load dependency entity for procedure Customdeath!");
			return;
		}
		if (dependencies.get("x") == null) {
			if (!dependencies.containsKey("x"))
				System.err.println("Failed to load dependency x for procedure Customdeath!");
			return;
		}
		if (dependencies.get("y") == null) {
			if (!dependencies.containsKey("y"))
				System.err.println("Failed to load dependency y for procedure Customdeath!");
			return;
		}
		if (dependencies.get("z") == null) {
			if (!dependencies.containsKey("z"))
				System.err.println("Failed to load dependency z for procedure Customdeath!");
			return;
		}
		if (dependencies.get("world") == null) {
			if (!dependencies.containsKey("world"))
				System.err.println("Failed to load dependency world for procedure Customdeath!");
			return;
		}
		Entity entity = (Entity) dependencies.get("entity");
		double x = dependencies.get("x") instanceof Integer ? (int) dependencies.get("x") : (double) dependencies.get("x");
		double y = dependencies.get("y") instanceof Integer ? (int) dependencies.get("y") : (double) dependencies.get("y");
		double z = dependencies.get("z") instanceof Integer ? (int) dependencies.get("z") : (double) dependencies.get("z");
		IWorld world = (IWorld) dependencies.get("world");
		if ((entity instanceof PlayerEntity)) {
			{
				MinecraftServer mcserv = ServerLifecycleHooks.getCurrentServer();
				if (mcserv != null)
					mcserv.getPlayerList()
							.sendMessage(new StringTextComponent((((entity.getDisplayName().getString())) + "" + ("ruined it for everyone..."))));
							// this sends the message to everyone, though may double up.
							// TODO: Reduce spam calling.
			}
			if (world instanceof ServerWorld)
				((ServerWorld) world).addLightningBolt(new LightningBoltEntity(world.getWorld(), (int) x, (int) y, (int) z, true));
			{
				List<? extends PlayerEntity> _players = world.getPlayers();
				for (Entity entityiterator : _players) {
					if ((entityiterator.isAlive())) {
						entityiterator.attackEntityFrom(DamageSource.LIGHTNING_BOLT, (float) 20000); // Deaths to everyone that's alive for non-repeated deaths.
						{
							Entity _ent = entity;
							if (_ent instanceof PlayerEntity) {
								Scoreboard _sc = ((PlayerEntity) _ent).getWorldScoreboard();
								ScoreObjective _so = _sc.getObjective("Global Caused Deaths");
								if (_so == null) {
									_so = _sc.addObjective("Global Caused Deaths", ScoreCriteria.DUMMY,
											new StringTextComponent("Global Caused Deaths"), ScoreCriteria.RenderType.INTEGER);
								}
								// This will be tracked.
								Score _scr = _sc.getOrCreateScore(((PlayerEntity) _ent).getScoreboardName(), _so);
								_scr.setScorePoints((int) ((new Object() {
									public int getScore(String score) {
										if (entity instanceof PlayerEntity) {
											Scoreboard _sc = ((PlayerEntity) entity).getWorldScoreboard();
											ScoreObjective _so = _sc.getObjective(score);
											if (_so != null) {
												Score _scr = _sc.getOrCreateScore(((PlayerEntity) entity).getScoreboardName(), _so);
												return _scr.getScorePoints();
											}
										}
										return 0;
									}
								}.getScore("Global Caused Deaths")) + 1));
							}
						}
					}
				}
			}
		}
	}

	@SubscribeEvent
	public void onEntityDeath(LivingDeathEvent event) {
		if (event != null && event.getEntity() != null) {
			Entity entity = event.getEntity();
			Entity sourceentity = event.getSource().getTrueSource();
			double i = entity.getPosX();
			double j = entity.getPosY();
			double k = entity.getPosZ();
			World world = entity.world;
			Map<String, Object> dependencies = new HashMap<>();
			dependencies.put("x", i);
			dependencies.put("y", j);
			dependencies.put("z", k);
			dependencies.put("world", world);
			dependencies.put("entity", entity);
			dependencies.put("sourceentity", sourceentity);
			dependencies.put("event", event);
			this.executeProcedure(dependencies);
		}
	}
}
