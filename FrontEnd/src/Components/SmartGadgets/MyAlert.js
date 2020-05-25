import React from "react";
import { Row, Alert, Col, ListGroup } from "react-bootstrap";
import FlashMessage from "react-flash-message";

function MyAlert(props) {
    let heading = props.heading ? (
        <Alert.Heading>{props.heading}</Alert.Heading>
    ) : null;
    let text = props.text ? props.text : null;
    let variant = props.variant ? props.variant : "success";

    const listOfItems = [];

    if (props.list) {
        for (let i = 0; i < props.list.length; i++) {
            listOfItems.push(<li>{props.list[i]}</li>);
        }
    }

    const listGroup = <ul>{listOfItems}</ul>;
    let list = props.list ? listGroup : null;

    const alert = (
        <Row className="d-flex justify-content-center">
            <Col xs={12} md={8}>
                <Alert variant={variant} className="mt-3">
                    {heading}
                    {text}
                    {list}
                </Alert>
            </Col>
        </Row>
    );

    const flash = (
        <FlashMessage duration={props.duration ? props.duration : 3000}>
            {alert}
        </FlashMessage>
    );
    return props.flash ? flash : alert;
}
export default MyAlert;
