package br.usp.ime.arranger;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.sun.xml.bind.AnyTypeAdapter;

@XmlRootElement
@XmlJavaTypeAdapter(AnyTypeAdapter.class)
public interface Behavior {

    void run() throws BehaviorException;

    void clear();
}