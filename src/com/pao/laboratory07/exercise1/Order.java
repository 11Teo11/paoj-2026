package com.pao.laboratory07.exercise1;

import com.pao.laboratory07.exercise1.exceptions.CannotCancelFinalOrderException;
import com.pao.laboratory07.exercise1.exceptions.CannotRevertInitialOrderStateException;
import com.pao.laboratory07.exercise1.exceptions.OrderIsAlreadyFinalException;

import java.util.Stack;

public class Order {
    private StareComanda currentState;
    private Stack<StareComanda> history;

    Order(StareComanda initialState){
        this.currentState = initialState;
        this.history = new Stack<>();
    }

    private boolean isFinalState(){
        return currentState == StareComanda.DELIVERED || currentState == StareComanda.CANCELED;
    }

    public void nextState() throws OrderIsAlreadyFinalException {
        if (isFinalState()){
            throw new OrderIsAlreadyFinalException("Order is already in a final state.");
        }

        history.push(currentState);
        switch (currentState){
            case PLACED -> currentState = StareComanda.PROCESSED;
            case PROCESSED -> currentState = StareComanda.SHIPPED;
            case SHIPPED -> currentState = StareComanda.DELIVERED;
        }

        System.out.println("Order state updated to: " + currentState);
    }

    public void cancel() throws CannotCancelFinalOrderException {
        if (isFinalState())
            throw new CannotCancelFinalOrderException("Cannot cancel a final state order.");
        history.push(currentState);
        currentState = StareComanda.CANCELED;
        System.out.println("Order has been canceled.");
    }

    public void undoState(){
        if (!history.empty()){
            currentState = history.pop();
            System.out.println("Order state reverted to: " + currentState);
        }
        else
            System.out.println("Nu există stare anterioară pentru undo.");
    }

}
