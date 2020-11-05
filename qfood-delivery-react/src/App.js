import logo from './logo.svg';
import './App.css';
import { useEffect, useState } from 'react';
import EventBus from 'vertx3-eventbus-client';
import MessageList from './MessageList';

function App() {

  const [messages, setMessages] = useState([]);

  useEffect(() => {
    var eventBus = new EventBus('http://localhost:8082/locations');
    eventBus.onopen = function () {
        eventBus.registerHandler('newLocation', function (error, message) {
            let location = JSON.parse(message.body);
            setMessages([...messages, location]);
        });
    }
  });


  return (
    <div className="App">
      <header className="App-header">
        <img src={logo} className="App-logo" alt="logo" />
        <p>
          Messages
        </p>
        <MessageList messages={messages} />
      </header>
    </div>
  );
}

export default App;
