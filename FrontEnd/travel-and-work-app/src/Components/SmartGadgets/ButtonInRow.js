import React from "react";
import { Modal, Button, Row } from "react-bootstrap";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { Link } from "react-router-dom";

function ButtonInRow(props) {
    let className = "d-flex m-3";
    if (props.side == "left") className += " flex-row";
    else if (props.side == "right") className += " flex-row-reverse";
    let backArrow = props.back ? <FontAwesomeIcon icon="chevron-left" /> : null;

    return (
        <Row className={className}>
            <Link to={props.link}>
                <Button variant={props.variant}>
                    {backArrow}
                    {props.label}
                </Button>
            </Link>
        </Row>
    );
}
export default ButtonInRow;
