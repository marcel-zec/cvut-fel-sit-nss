import React from "react";
import { Form, Col } from "react-bootstrap";
import AchievementInput from "./AchievementInput";

class AchievementFormGroup extends React.Component {
    render() {
        let itemsArray = [];

        /*console.log("ACHIEVEMENT FORM GROUP");
        console.log(this.props.items);
        console.log(this.props.selected);*/

        if (this.props.multiple) {
            for (let key in this.props.items) {
                let items = [];
                items.push(
                    <Form.Label className="mt-2">
                        {capitalizeFirstLetter(key)}
                    </Form.Label>
                );

                if (this.props.items[key]) {
                    this.props.items[key].forEach((element) => {
                        let found = false;
                        if (this.props.selected) {
                            if (Array.isArray(this.props.selected)) {
                                found = this.props.selected.find(
                                    (item) => item.id == element.id
                                );
                            } else if (this.props.selected[key]) {
                                found = this.props.selected[key].find(
                                    (item) => item.id == element.id
                                );
                            }
                        }

                        items.push(
                            <AchievementInput
                                element={element}
                                selected={found}
                                onChangeMethod={(event) =>
                                    this.props.onChangeMethod[key](event)
                                }
                                uneditable={
                                    this.props.uneditable ? true : false
                                }
                            />
                        );
                    });
                    console.log(items);
                    itemsArray.push(items);
                }
            }

            /*if(this.props.items.hasOwnProperty("special")){

            }
            if(this.props.items.hasOwnProperty("certificate")){

            }
            if(this.props.items.hasOwnProperty("categorized")){

            }*/
        } else if (this.props.items.length > 0) {
            this.props.items.forEach((element) => {
                let found = false;
                if (this.props.selected) {
                    found = this.props.selected.find(
                        (item) => item.id == element.id
                    );
                }

                itemsArray.push(
                    <AchievementInput
                        element={element}
                        selected={found}
                        onChangeMethod={(event) =>
                            this.props.onChangeMethod(event)
                        }
                        uneditable={this.props.uneditable ? true : false}
                    />
                );
            });
        }

        return (
            <Form.Group as={Col}>
                <Form.Label>{this.props.label}</Form.Label>
                <div className="d-flex flex-column align-items-start ml-5">
                    {itemsArray}
                </div>
            </Form.Group>
        );
    }
}

function capitalizeFirstLetter(string) {
    return string.charAt(0).toUpperCase() + string.slice(1);
}
export default AchievementFormGroup;
