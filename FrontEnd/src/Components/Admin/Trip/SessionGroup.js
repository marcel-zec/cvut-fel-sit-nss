import React from "react";
import { Form, Button } from "react-bootstrap";
import SessionInput from "./UI/SessionInput";
import ButtonInRow from "../../SmartGadgets/ButtonInRow";

class SessionGroup extends React.Component {
    addNewSessionHandler = () => {
        let session = {
            index: null,
            date_from: null,
            date_to: null,
            capacity: null,
        };
        this.props.onChangeMethod(session);
    };

    render() {
        let arraySessions = null;
        if (this.props !== null) {
            arraySessions = [];
            console.log(this.props.sessions);
            const sessions = this.props.sessions;
            for (let i = 0; i < sessions.length; i++) {
                if (!sessions[i].index) sessions[i].index = i;
                arraySessions.push(
                    <SessionInput
                        onChangeMethod={this.props.onChangeMethod}
                        forDeleteSession={this.props.forDeleteSession}
                        session={sessions[i]}
                    />
                );
            }
        }

        return (
            <>
                <Form.Row>
                    <ButtonInRow
                        variant="success"
                        side="left"
                        label="Add session"
                        onClickMethod={() => this.addNewSessionHandler()}
                    />
                </Form.Row>
                {arraySessions}
            </>
        );
    }
}
export default SessionGroup;
