const AC_GAME_OBJECT = [];
// Base class for game objects, all game objects inherit from it
export default class AcGameObject {
  constructor() {
    AC_GAME_OBJECT.push(this);
  }

  // Execute only once
  start() {}

  // Execute once per frame
  update() {}

  // Execute before deletion
  on_destroy() {}

  destroy() {
    this.on_destroy();
    const index = AC_GAME_OBJECT.indexOf(this);
    if (index !== -1) {
      AC_GAME_OBJECT.splice(index, 1);
    }
  }
}
let last_timestamp; // Time of the last execution

/**
 * Executes the game loop for each frame.
 * @param {number} timestamp - The timestamp of the current frame.
 */
const step = (timestamp) => {
  for (let obj of AC_GAME_OBJECT) {
    // If the object has not been started, call the start method; otherwise, call the update method.
    if (!obj.has_called_start) {
      obj.has_called_start = true;
      obj.start();
    } else {
      obj.timedelta = timestamp - last_timestamp;
      obj.update();
    }
  }
  last_timestamp = timestamp;
  requestAnimationFrame(step);
};
//timestamp 是由 requestAnimationFrame 传给回调函数的，表示回调队列被触发的时间
//一秒60帧
requestAnimationFrame(step);
