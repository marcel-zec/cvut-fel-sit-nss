import React from "react";
import { Modal, Button } from "react-bootstrap";
import ModalCentered from "./ModalCentered";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import "./Modal.css";

function ErrorMessage(props) {
    return <div className={props.show ? "" : "d-none"}>{props.message}</div>;
}
export default ErrorMessage;
