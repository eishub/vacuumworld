package gui;

import grid.Direction;
import vac.VacBot;
import vac.VacWorld;
import actions.ImpossibleActionException;
import actions.UnavailableActionException;

public final class Demo {

	private VacWorld world;

	public static void main(String[] args) {
		new Demo();
	}

	private Demo() {
		world = VacWorld.createFromConfig("demo.conf");
		world.show();

		VacBot[] vacBots = world.getVacBots();
		for (VacBot vacBot : vacBots) {
			// new StreamEventLogger(vacBot, System.out);
			new RandomWalker(vacBot).start();
		}

	}

	private static class RandomWalker extends Thread {
		private final VacBot vacBot;

		RandomWalker(VacBot vacBot) {
			this.vacBot = vacBot;
		}

		public void run() {
			while (true) {
				try {
					vacBot.clean();
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (UnavailableActionException e) {
					// No dust here - never mind, move on
				}
				try {
					vacBot.move(1, Direction.random());
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (ImpossibleActionException e) {
					// Move not possible - never mind, try something else
				} catch (UnavailableActionException e) {
					// Move temporarily blocked - never mind, try something else
				}
				for (int i = 0; i < 4; i++) {
					try {
						Thread.sleep(200);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					vacBot.setLightOn(true);
					try {
						Thread.sleep(200);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					vacBot.setLightOn(false);
				}
			}
		}
	}
}
