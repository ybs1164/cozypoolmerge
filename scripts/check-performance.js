const assert = require('node:assert/strict');
const fs = require('node:fs');
const path = require('node:path');
const vm = require('node:vm');
const html = fs.readFileSync(path.join(__dirname, '..', 'index.html'), 'utf8');
for (const match of html.matchAll(/<script\b[^>]*>([\s\S]*?)<\/script>/g)) {
  new vm.Script(match[1]);
}
const physics = html.slice(html.indexOf('  function physicsStep(dt){'), html.indexOf('  // Cute idle tilt'));
const original = physics.replace(
  'if (dx * dx + dy * dy >= minDist * minDist) continue;',
  'const dist = Math.hypot(dx, dy) || 0.0001; if (dist >= minDist) continue;'
).replace('          const dist = Math.hypot(dx, dy) || 0.0001;', '');
function run(source, bodies) {
  const events = [];
  const noop = () => {};
  const context = { slimes: structuredClone(bodies), gameOver: false, trackReady: false,
    launcherPos: { x: -10000, y: -10000 }, launcher: {}, DANGER_RADIUS: 10,
    GAME_OVER_WAIT: 3, BOUNCE: 0.8, DEER_LEVEL: 6, COLLISION_FX_MIN_SPEED: 60, COLLISION_SCORE: 1,
    SLIME_DATA: Object.fromEntries(Array.from({length: 10}, (_, i) => [i + 1, {}])),
    getSlimeData: level => ({score: level * 10, color: '#ffffff'}),
    GameManager: {addScore: score => events.push(['score', score])},
    spawnSlimeAt: (...args) => events.push(['spawn', ...args]),
    spawnMergeParticles: noop, spawnCollisionParticles: noop, spawnPop: noop,
    playMergeSound: noop, playCollisionSound: noop, triggerHapticFeedback: noop,
    tutorialOnMerge: noop, pulseEvolution: noop, spinKick: noop,
    updateSlimeAnim: noop, updateRipples: noop, triggerGameOver: noop };
  vm.runInNewContext(source + '\nphysicsStep(1 / 120);', context);
  return JSON.parse(JSON.stringify({ slimes: context.slimes, events }));
}
let seed = 17;
function random() { seed = (Math.imul(seed, 1664525) + 1013904223) >>> 0; return seed / 2 ** 32; }
for (let trial = 0; trial < 200; trial++) {
  const bodies = Array.from({length: 40}, () => ({ x: random() * 500, y: random() * 500,
    vx: random() * 400 - 200, vy: random() * 400 - 200, radius: 10 + random() * 40,
    mass: 1 + random() * 5, level: 1 + Math.floor(random() * 6), isMerging: false }));
  assert.deepEqual(run(physics, bodies), run(original, bodies));
}
const body = (x, level) => ({x, y: 0, vx: 0, vy: 0, radius: 10, mass: 1, level, isMerging: false});
for (const bodies of [[body(0, 1), body(20, 1)], [body(0, 1), body(0, 1)],
  [body(0, 1), body(0, 2)], [body(0, 6), body(1, 6)]]) {
  assert.deepEqual(run(physics, bodies), run(original, bodies));
}
console.log('Script syntax and 204 collision/merge equivalence cases passed.');
