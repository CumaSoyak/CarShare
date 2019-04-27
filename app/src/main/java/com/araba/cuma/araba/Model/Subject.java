package com.araba.cuma.araba.Model;

import java.util.Observer;

public interface Subject {
    public void incomingMessage(Observer observer);
    public void notifyObservers();
}
