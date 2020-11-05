import React from 'react'

class MessageList extends React.Component {
    render() {
        let elements = this.props.messages.map((element, index) => {
            let {latitude, longitude} = element;
            return (<li key={index}>Latitude: {latitude} - Longitude: {longitude}</li>)
        })
        return <ul>{elements}</ul>
    }
}

export default MessageList