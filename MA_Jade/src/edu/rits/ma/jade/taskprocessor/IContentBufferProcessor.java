package edu.rits.ma.jade.taskprocessor;

import edu.rits.ma.jade.communication.ContentIncomingBuffer;
import edu.rits.ma.jade.communication.ContentOutcomingBuffer;

public interface IContentBufferProcessor {
	public void processCommunicationDataBuffer(ContentIncomingBuffer receiveBuffer, ContentOutcomingBuffer sendBuffer);
	public boolean done();
}
