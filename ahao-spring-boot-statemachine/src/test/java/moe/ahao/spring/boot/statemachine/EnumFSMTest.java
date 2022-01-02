package moe.ahao.spring.boot.statemachine;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Objects;

/**
 * 使用枚举来实现有限状态机
 * | Index | 现态       | 条件/事件 | 动作         | 次态 |
 * |:-----:|:----------|:-------:|:-----------:|:------:|
 * | 1     | LOCKED    | COIN    | OPENED      | UN_LOCKED  |
 * | 2     | LOCKED    | PASS    | ALARM       | LOCKED     |
 * | 3     | UN_LOCKED | COIN    | REFUND      | UN_LOCKED  |
 * | 4     | UN_LOCKED | PASS    | CLOSED      | LOCKED     |
 */
public class EnumFSMTest {
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

    private enum State {
        UNLOCKED {
            @Override
            public Result coin(FSM fsm) {
                fsm.state = this;
                return fsm.refund();
            }

            @Override
            public Result pass(FSM fsm) {
                fsm.state = LOCKED;
                return fsm.close();
            }
        },
        LOCKED {
            @Override
            public Result coin(FSM fsm) {
                fsm.state = UNLOCKED;
                return fsm.open();
            }

            @Override
            public Result pass(FSM fsm) {
                fsm.state = this;
                return fsm.alarm();
            }
        };
        public abstract Result coin(FSM fsm);
        public abstract Result pass(FSM fsm);
    }
    private enum Event {
        COIN {
            @Override
            public Result execute(FSM fsm, State state) {
                return state.coin(fsm);
            }
        },
        PASS {
            @Override
            public Result execute(FSM fsm, State state) {
                return state.pass(fsm);
            }
        };
        public abstract Result execute(FSM fsm, State state);
    }
    private enum Result {REFUND, CLOSED, ALARM, OPENED}
    private static class FSM {
        private State state;
        public FSM(State state) {
            this.state = state;
        }
        public Result execute(Event event) {
            if (Objects.isNull(event)) {
                throw new RuntimeException();
            }
            return event.execute(this, state);
        }
        public Result open() {
            return Result.OPENED;
        }

        public Result alarm() {
            return Result.ALARM;
        }

        public Result refund() {
            return Result.REFUND;
        }

        public Result close() {
            return Result.CLOSED;
        }
    }
}
