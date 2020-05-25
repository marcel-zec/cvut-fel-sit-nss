import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";

import React from "react";

function AchievementListInline(props){
    const [modalShow, setModalShow] = React.useState(false);
    let toReturn = [];

    if(props.achievements.length == 0){
        return <div>{props.message}</div>
    }
    props.achievements.forEach(achievement => {
        toReturn.push(
            <div className="customTooltip">
                <FontAwesomeIcon icon={achievement.icon} />
                {userHasAchievement(achievement,props.userList)}
                <div>{achievement.name}
                </div>
                <span className="tooltiptext tooltip-bottom">{achievement.description}</span>
                
            </div>
        )
    });
    return toReturn;

}
function userHasAchievement(achievement,userList){
    if(userList.some(item => item.id == achievement.id)){
        return <FontAwesomeIcon className="checked" icon="check-circle"/>;
    }else{
        return <FontAwesomeIcon className="false" icon="minus-circle"/>;
    }   
}
export default AchievementListInline;