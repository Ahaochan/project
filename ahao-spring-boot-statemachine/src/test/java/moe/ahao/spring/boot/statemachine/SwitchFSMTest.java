package moe.ahao.spring.boot.statemachine;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * 使用Switch来实现有限状态机
 * | Index | 现态       | 条件/事件 | 动作         | 次态 |
 * |:-----:|:----------|:-------:|:-----------:|:------:|
 * | 1     | LOCKED    | COIN    | OPENED      | UN_LOCKED  |
 * | 2     | LOCKED    | PASS    | ALARM       | LOCKED     |
 * | 3     | UN_LOCKED | COIN    | REFUND      | UN_LOCKED  |
 * | 4     | UN_LOCKED | PASS    | CLOSED      | LOCKED     |
 */
public class SwitchFSMTest {
    @Test
    void index1() {
        FSM fsm = new FSM(State.LOCKED);
        Result result = fsm.execute(Event.COIN);

        Assertions.assertEquals(Result.OPENED, result);
        Assertions.assertEquals(State.UNLOCKED, fsm.state);
    }

    @Test
    void index2() {
        FSM fsm = new FSM(State.LOCKED);
        Result result = fsm.execute(Event.PASS);

        Assertions.assertEquals(Result.ALARM, result);
        Assertions.assertEquals(State.LOCKED, fsm.state);
    }

    @Test
    void index3() {
        FSM fsm = new FSM(State.UNLOCKED);
        Result result = fsm.execute(Event.COIN);

        Assertions.assertEquals(Result.REFUND, result);
        Assertions.assertEquals(State.UNLOCKED, fsm.state);
    }

    @Test
    void index4() {
        FSM fsm = new FSM(State.UNLOCKED);
        Result result = fsm.execute(Event.PASS);

        Assertions.assertEquals(Result.CLOSED, result);
        Assertions.assertEquals(State.LOCKED, fsm.state);
    }

    private enum State {UNLOCKED, LOCKED}
    private enum Event {COIN, PASS}
    private enum Result {REFUND, CLOSED, ALARM, OPENED}
    private static class FSM {
        public State state;
        public FSM(State state) {
            this.state = state;
        }
        public Result execute(Event event) {
            if (event == null) {
                throw new RuntimeException();
            }
            if (State.LOCKED.equals(state)) {
                switch (event) {
                    case COIN:
                        this.state = State.UNLOCKED;
                        return Result.OPENED;
                    case PASS:
                        return Result.ALARM;
                }
            }
            if (State.UNLOCKED.equals(state)) {
                switch (event) {
                    case PASS:
                        this.state = State.LOCKED;
                        return Result.CLOSED;
                    case COIN:
                        return Result.REFUND;
                }
            }
            return null;
        }
    }
}
