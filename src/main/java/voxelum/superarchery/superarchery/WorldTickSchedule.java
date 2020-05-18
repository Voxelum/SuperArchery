package voxelum.superarchery.superarchery;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

@EventBusSubscriber
public class WorldTickSchedule {
    private static Queue<Runnable> nextTicks = new ArrayDeque<>();
    private static List<Runnable> repeats = new ArrayList<>();

    private static class RepeatWithInterval<T> {
        StateRunner<T> task;
        T state;
        int interval;
        int left = 0;

        RepeatWithInterval(StateRunner<T> task, T state, int interval) {
            this.task = task;
            this.state = state;
            this.interval = interval;
        }

        void cancel() {
            Runnable r = this::run;
            repeats.remove(r);
        }

        void run() {
            if (left == 0) {
                task.run(state, this::cancel);
                left = interval;
            } else {
                left -= 1;
            }
        }
    }

    public interface StateRunner<T> {
        T run(T state, Runnable resolve);
    }

    public static void next(Runnable r) {
        nextTicks.offer(r);
    }

    /**
     * Repeat an operation for each world tick
     * 
     * @param r The operation
     * @return The remove handler for this operation.
     */
    public static Runnable repeat(Runnable r) {
        repeats.add(r);
        return () -> {
            repeats.remove(r);
        };
    }

    /**
     * Repeat an operation for each world tick until the runner decide to stop
     * 
     * @param r The operation
     * @return The remove handler for this operation.
     */
    public static <T> Runnable repeat(StateRunner<T> r, T state) {
        RepeatWithInterval<T> repeat = new RepeatWithInterval<>(r, state, 0);
        repeats.add(repeat::run);
        return repeat::cancel;
    }

    /**
     * Repeat an operation for each world tick until the runner decide to stop
     * 
     * @param r The operation
     * @return The remove handler for this operation.
     */
    public static Runnable repeat(StateRunner<Integer> r, int state) {
        return repeat(r, Integer.valueOf(state));
    }

    /**
     * Repeat an operation for each interval of world tick until the runner decide
     * to stop
     * 
     * @param r The operation
     * @return The remove handler for this operation.
     */
    public static <T> Runnable repeatInterval(int interval, StateRunner<T> r, T state) {
        RepeatWithInterval<T> repeat = new RepeatWithInterval<>(r, state, interval);
        repeats.add(repeat::run);
        return repeat::cancel;
    }

    /**
     * Repeat an operation for each interval of world tick until the runner decide
     * to stop
     * 
     * @param r The operation
     * @return The remove handler for this operation.
     */
    public static Runnable repeatInterval(int interval, StateRunner<Integer> r, int state) {
        return repeatInterval(interval, r, Integer.valueOf(state));
    }

    @SubscribeEvent
    public static void onWorldTick(TickEvent.WorldTickEvent event) {
        if (event.side.isClient() || event.phase == TickEvent.Phase.START) {
            return;
        }

        Queue<Runnable> nextWorldTick = new ArrayDeque<>(nextTicks);
        nextTicks.clear();
        while (!nextWorldTick.isEmpty()) {
            nextWorldTick.poll().run();
        }

        for (Runnable r : new ArrayList<>(repeats)) {
            r.run();
        }
    }
}