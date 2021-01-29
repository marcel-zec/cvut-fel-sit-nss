import React from "react";
import { Modal, Button } from "react-bootstrap";
import ModalCentered from "./ModalCentered";
import "./Modal.css";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";


function AchievementModal(props) {
    const [modalShow, setModalShow] = React.useState(false);

    /* <Button variant="primary" onClick={() => setModalShow(true)}>
                Launch vertically centered modal
            </Button>*/

    const iconClass = "ml-2 mr-2";

    let modalButton = (
        //clickable icon with modal window on click
        <FontAwesomeIcon
            className={iconClass + " modalButton"}
            icon={props.icon}
            size="lg"
            onClick={() => setModalShow(true)}
        />
    );
    if (props.titleBeforeIcon) {
        //clickable div with title and icon with modal window on click
        modalButton = (
            <div className="modalButton" onClick={() => setModalShow(true)}>
                {props.title}
                <FontAwesomeIcon
                    className={iconClass}
                    icon={props.icon}
                    size="lg"
                />
            </div>
        );
    }

    return (
        <>
            {modalButton}

            <ModalCentered
                show={modalShow}
                onHide={() => setModalShow(false)}
                title={props.title}
                description={props.description}
                icon={props.icon}
            />
        </>
    );
}
export default AchievementModal;
