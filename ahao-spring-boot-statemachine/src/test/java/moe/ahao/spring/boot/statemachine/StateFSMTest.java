package moe.ahao.spring.boot.statemachine;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * 使用State模式来实现有限状态机
 * | Index | 现态       | 条件/事件 | 动作         | 次态 |
 * |:-----:|:----------|:-------:|:-----------:|:------:|
 * | 1     | LOCKED    | COIN    | OPENED      | UN_LOCKED  |
 * | 2     | LOCKED    | PASS    | ALARM       | LOCKED     |
 * | 3     | UN_LOCKED | COIN    | REFUND      | UN_LOCKED  |
 * | 4     | UN_LOCKED | PASS    | CLOSED      | LOCKED     |
 */
public class StateFSMTest {
    @Test
    void index1() {
        FSM fsm = new FSM(FSM.LOCKED);
        Result result = fsm.execute(Event.COIN);

        Assertions.assertEquals(Result.OPENED, result);
        Assertions.assertTrue(fsm.isUnlocked());
    }

    @Test
    void index2() {
        FSM fsm = new FSM(FSM.LOCKED);
        Result result = fsm.execute(Event.PASS);

        Assertions.assertEquals(Result.ALARM, result);
        Assertions.assertTrue(fsm.isLocked());
    }

    @Test
    void index3() {
        FSM fsm = new FSM(FSM.UNLOCKED);
        Result result = fsm.execute(Event.COIN);

        Assertions.assertEquals(Result.REFUND, result);
        Assertions.assertTrue(fsm.isUnlocked());
    }

    @Test
    void index4() {
        FSM fsm = new FSM(FSM.UNLOCKED);
        Result result = fsm.execute(Event.PASS);

        Assertions.assertEquals(Result.CLOSED, result);
        Assertions.assertTrue(fsm.isLocked());
    }

    private interface State {
        Result coin(FSM fsm);
        Result pass(FSM fsm);
    }
    private enum Event {COIN, PASS}
    private enum Result {REFUND, CLOSED, ALARM, OPENED}

    private static class LockedState implements State {
        @Override
        public Result coin(FSM fsm) {
            return fsm.open();
        }
        @Override
        public Result pass(FSM fsm) {
            return fsm.alarm();
        }
    }
    private static class UnlockedState implements State {
        @Override
        public Result coin(FSM fsm) {
            return fsm.refund();
        }

        @Override
        public Result pass(FSM fsm) {
            return fsm.close();
        }
    }
    private static class FSM {
        public static final State LOCKED = new LockedState();
        public static final State UNLOCKED = new UnlockedState();
        public State state;
        public FSM(State state) {
            this.state = state;
        }
        public Result execute(Event event) {
            if (event == null) {
                throw new RuntimeException();
            }
            if (Event.PASS.equals(event)) {
                return state.pass(this);
            }
            return state.coin(this);
        }
        public boolean isUnlocked() {
            return state == UNLOCKED;
        }
        public boolean isLocked() {
            return state == LOCKED;
        }
        public Result open() {
            this.state = UNLOCKED;
            return Result.OPENED;
        }
        public Result alarm() {
            this.state = LOCKED;
            return Result.ALARM;
        }

        public Result refund() {
            this.state = UNLOCKED;
            return Result.REFUND;
        }

        public Result close() {
            this.state = LOCKED;
            return Result.CLOSED;
        }
    }
}
