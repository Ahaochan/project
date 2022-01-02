package moe.ahao.spring.boot.statemachine;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

/**
 * 使用状态集合来实现有限状态机
 * | Index | 现态       | 条件/事件 | 动作         | 次态 |
 * |:-----:|:----------|:-------:|:-----------:|:------:|
 * | 1     | LOCKED    | COIN    | OPENED      | UN_LOCKED  |
 * | 2     | LOCKED    | PASS    | ALARM       | LOCKED     |
 * | 3     | UN_LOCKED | COIN    | REFUND      | UN_LOCKED  |
 * | 4     | UN_LOCKED | PASS    | CLOSED      | LOCKED     |
 */
public class StateCollectionFSMTest {
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
        List<FSMTransaction> transactions = Arrays.asList(
            new FSMTransaction(State.LOCKED, Event.COIN, new OpenAction(), State.UNLOCKED),
            new FSMTransaction(State.LOCKED, Event.PASS, new AlarmAction(), State.LOCKED),
            new FSMTransaction(State.UNLOCKED, Event.PASS, new CloseAction(), State.LOCKED),
            new FSMTransaction(State.UNLOCKED, Event.COIN, new RefundAction(), State.UNLOCKED)
            );
        public State state;
        public FSM(State state) {
            this.state = state;
        }
        public Result execute(Event event) {
            FSMTransaction transaction = transactions.stream()
                .filter(t -> t.event.equals(event))
                .filter(t -> t.currentState.equals(state))
                .findFirst()
                .orElse(null);
            if (transaction == null) {
                throw new RuntimeException();
            }
            this.state = transaction.nextState;
            return transaction.action.execute();
        }
    }
    private static class FSMTransaction {
        public State currentState;
        public Event event;
        public Action action;
        public State nextState;

        public FSMTransaction(State currentState, Event event, Action e, State nextState) {
            this.currentState = currentState;
            this.event = event;
            this.action = e;
            this.nextState = nextState;
        }
    }

    private interface Action {
        Result execute();
    }
    private static class OpenAction implements Action {
        @Override
        public Result execute() {
            return Result.OPENED;
        }
    }
    private static class AlarmAction implements Action {
        @Override
        public Result execute() {
            return Result.ALARM;
        }
    }
    private static class CloseAction implements Action {
        @Override
        public Result execute() {
            return Result.CLOSED;
        }
    }
    private static class RefundAction implements Action {
        @Override
        public Result execute() {
            return Result.REFUND;
        }
    }
}
